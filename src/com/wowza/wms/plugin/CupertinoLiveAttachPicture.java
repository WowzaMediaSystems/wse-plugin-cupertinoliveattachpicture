/**
 * Wowza server software and all components Copyright 2006 - 2015, Wowza Media Systems, LLC, licensed pursuant to the Wowza Media Software End User License Agreement.
 */
package com.wowza.wms.plugin;

import java.io.File;

import com.wowza.wms.application.IApplicationInstance;
import com.wowza.wms.httpstreamer.cupertinostreaming.livestreampacketizer.LiveStreamPacketizerCupertino;
import com.wowza.wms.logging.WMSLogger;
import com.wowza.wms.logging.WMSLoggerFactory;
import com.wowza.wms.logging.WMSLoggerIDs;
import com.wowza.wms.media.mp3.model.idtags.ID3Frames;
import com.wowza.wms.media.mp3.model.idtags.ID3V2FrameAttachedPicture;
import com.wowza.wms.module.ModuleBase;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizer;
import com.wowza.wms.stream.livepacketizer.ILiveStreamPacketizerActionNotify;

public class CupertinoLiveAttachPicture extends ModuleBase
{
	public static final String MODULE_NAME = "ModuleCupertinoLiveAttachPicture";
	public static final String PROP_NAME_PREFIX = "cupertinoAttachPicture";
	
	public static final String[] EXTENSIONS = { ".jpg", ".jpeg", ".png" };

	private WMSLogger logger = null;
	private boolean debugLog = false;

	class LiveActionNotify implements ILiveStreamPacketizerActionNotify
	{
		private IApplicationInstance appInstance = null;

		public LiveActionNotify(IApplicationInstance appInstance)
		{
			this.appInstance = appInstance;
		}

		public void onLiveStreamPacketizerCreate(ILiveStreamPacketizer liveStreamPacketizer, String streamName)
		{
		}

		public void onLiveStreamPacketizerDestroy(ILiveStreamPacketizer liveStreamPacketizer)
		{
		}

		public void onLiveStreamPacketizerInit(ILiveStreamPacketizer liveStreamPacketizer, String streamName)
		{
			while (true)
			{
				if (!(liveStreamPacketizer instanceof LiveStreamPacketizerCupertino))
					break;

				LiveStreamPacketizerCupertino cupertinoPacketizer = (LiveStreamPacketizerCupertino)liveStreamPacketizer;

				ID3Frames id3HeaderAudio = cupertinoPacketizer.getID3FramesHeaderAudio();

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

		appInstance.addLiveStreamPacketizerListener(new LiveActionNotify(appInstance));
		logger.info(MODULE_NAME + ".onAppStart[" + appInstance.getContextStr() + "]", WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);
	}

	public void onAppStop(IApplicationInstance appInstance)
	{
		logger.info(MODULE_NAME + ".onAppStop[" + appInstance.getContextStr() + "]", WMSLoggerIDs.CAT_application, WMSLoggerIDs.EVT_comment);
	}
}