/**
 * Wowza server software and all components Copyright 2006 - 2015, Wowza Media Systems, LLC, licensed pursuant to the Wowza Media Software End User License Agreement.
 */
package com.wowza.wms.plugin;

import java.io.File;

import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.httpstreamer.cupertinostreaming.file.IHTTPStreamerCupertinoIndex;
import com.wowza.wms.httpstreamer.cupertinostreaming.file.IHTTPStreamerCupertinoIndexItem;
import com.wowza.wms.httpstreamer.cupertinostreaming.httpstreamer.HTTPStreamerApplicationContextCupertinoStreamer;
import com.wowza.wms.httpstreamer.cupertinostreaming.httpstreamer.IHTTPStreamerCupertinoVODActionNotify;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertinoChunk;
import com.wowza.wms.httpstreamer.model.IHTTPStreamerApplicationContext;
import com.wowza.wms.httpstreamer.model.IHTTPStreamerSession;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.logging.WMSLoggerIDs;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameAttachedPicture;
import com.wowza.wms.module.ModuleBase;

public class CupertinoVODAttachPicture extends ModuleBase
{
	public static final String MODULE_NAME = "ModuleCupertinoVODAttachPicture";
	public static final String PROP_NAME_PREFIX = "cupertinoAttachPicture";
	
	public static final String[] EXTENSIONS = { ".jpg", ".jpeg", ".png" };

	private WMSLogger logger = null;
	private boolean debugLog = false;

	class VODActionNotify implements IHTTPStreamerCupertinoVODActionNotify
	{
		IApplicationInstance appInstance = null;

		public VODActionNotify(IApplicationInstance appInstance)
		{
			this.appInstance = appInstance;
		}

		public void onCreate(IHTTPStreamerCupertinoIndex fileIndex, IHTTPStreamerApplicationContext appContext, IHTTPStreamerSession httpStreamerSession, String rawStreamName, String streamExt, String streamName)
		{
		}

		public void onInit(IHTTPStreamerCupertinoIndex fileIndex, IHTTPStreamerApplicationContext appContext, IHTTPStreamerSession httpStreamerSession, String rawStreamName, String streamExt, String streamName)
		{
		}

		public void onOpen(IHTTPStreamerCupertinoIndex fileIndex, IHTTPStreamerApplicationContext appContext, IHTTPStreamerSession httpStreamerSession, String rawStreamName, String streamExt, String streamName)
		{
		}

		public void onIndex(IHTTPStreamerCupertinoIndex fileIndex, IHTTPStreamerApplicationContext appContext, IHTTPStreamerSession httpStreamerSession, String rawStreamName, String streamExt, String streamName)
		{
			if (streamName.lastIndexOf(".") > 0)
				streamName = streamName.substring(0, streamName.lastIndexOf("."));
			
			while (true)
			{
				ID3Frames id3HeaderAudio = fileIndex.getStreamMode() == LiveStreamPacketizerCupertino.STREAMMODE_TS ? fileIndex.getID3FramesHeaderAudio() : fileIndex.getID3FramesHeader();

				if (id3HeaderAudio == null)
					break;

				String storagePath = appInstance.getStreamStoragePath();

				File file = null;
				for (int i = 0; i < EXTENSIONS.length; i++)
				{
					file = new File(storagePath + "/" + streamName + EXTENSIONS[i]);
					if (file.exists())
						break;
					else
						file = null;
				}

				if (file == null)
					break;

				ID3V2FrameAttachedPicture attachedPicture = new ID3V2FrameAttachedPicture();
				boolean success = attachedPicture.loadFile(file);

				if (debugLog)
					logger.info(MODULE_NAME + ".success: " + success, WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);

				if (success)
				{
					id3HeaderAudio.putFrame(attachedPicture);

					if (debugLog)
						logger.info(MODULE_NAME + ".picture: " + file.getName(), WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);
				}
				break;
			}
		}

		public void onFillChunkStart(IHTTPStreamerCupertinoIndex fileIndex, IHTTPStreamerCupertinoIndexItem item, LiveStreamPacketizerCupertinoChunk chunk, boolean audioOnly)
		{
		}

		public void onFillChunkEnd(IHTTPStreamerCupertinoIndex fileIndex, IHTTPStreamerCupertinoIndexItem item, LiveStreamPacketizerCupertinoChunk chunk, boolean audioOnly)
		{
		}

		public void onDestroy(IHTTPStreamerCupertinoIndex fileIndex)
		{
		}
	}

	public void onAppStart(IApplicationInstance appInstance)
	{
		logger = WMSLoggerFactory.getLoggerObj(appInstance);
		// old property name
		debugLog = appInstance.getProperties().getPropertyBoolean("debugLoggingPosterFrames", debugLog);
		//new property name
		debugLog = appInstance.getProperties().getPropertyBoolean(PROP_NAME_PREFIX + "DebugLog", debugLog);
		
		if(logger.isDebugEnabled())
			debugLog = true;

		while (true)
		{
			IHTTPStreamerApplicationContext appContext = appInstance.getHTTPStreamerApplicationContext("cupertinostreaming", true);
			if (appContext == null)
				break;

			if (!(appContext instanceof HTTPStreamerApplicationContextCupertinoStreamer))
				break;

			HTTPStreamerApplicationContextCupertinoStreamer cupertinoAppContext = (HTTPStreamerApplicationContextCupertinoStreamer)appContext;
			cupertinoAppContext.addVODActionListener(new VODActionNotify(appInstance));
			break;
		}

		logger.info(MODULE_NAME + ".onAppStart[" + appInstance.getContextStr() + "]", WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);
	}

	public void onAppStop(IApplicationInstance appInstance)
	{
		logger.info(MODULE_NAME + ".onAppStop[" + appInstance.getContextStr() + "]", WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);
	}
}