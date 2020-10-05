# CupertinoLiveAttachPicture
The **ModuleCupertinoLiveAttachPicture** module for [Wowza Streaming Engine™ media server software](https://www.wowza.com/products/streaming-engine) can be used to add a poster image to audio-only iOS streams that are produced to comply with [Apple App Store rules for 3G delivery](https://www.wowza.com/docs/How-to-create-Apple-App-Store-compliant-streams) as well as for live and on-demand streams that don't comply with the Apple App Store rules.

This repo includes a [compiled version](/lib/wse-plugin-cupertinoliveattachpicture.jar).

## Prerequisites
Wowza Streaming Engine 4.0.0 or later is required.

## Usage
There are three ways you can add a poster image to an iOS audio-only stream by using the **CupertinoLiveAttachPicture** module. To add a poster image to an audio-only iOS stream, use:

* **CupertinoLiveAttachPicture** - For streams that are produced by using the audio-only rendition of the [App Store compliance](https://www.wowza.com/docs/How-to-create-Apple-App-Store-compliant-streams) technique.

* **CupertinoLiveAttachPicture2** - For live audio-only streams that don't use the App Store compliance technique.

* **CupertinoVODAttachPicture** - For on-demand audio streams that don't use the App Store compliance technique.

Poster images are added through the addition of ID3 metadata to the stream that's interpreted by the iOS player. The module searches for an image file in the Wowza media server's **[install-dir]/content** folder with the name **[stream-name].[extension]**, where **[extension]** can be **jpg**, **jpeg**, or **png**. For example, if your stream is named **myStream** and your image is in the JPG format, your poster image file must be named **myStream.jpg** for the module to use it as the poster image.

> **Note:** You can use the Wowza Streaming Engine server-side API to include a broader set of ID3 metadata in your stream. For details see the **com.wowza.wms.media.mp3.model.idtags** package in the [Wowza Streaming Engine Server-Side API Reference](https://www.wowza.com/resources/serverapi/).

For streams that comply with the App Store, you can test by using the following playback URL. Note that the _?wowzaaudioonly_ querystring at the end of the URL is required.
<pre>http://[wowza-ip-address]:1935/live/myStream/playlist.m3u8?wowzaaudioonly</pre>
	
## More resources
[Wowza Streaming Engine Server-Side API Reference](https://www.wowza.com/resources/serverapi/)

[How to extend Wowza Streaming Engine using the Wowza IDE](https://www.wowza.com/docs/how-to-extend-wowza-streaming-engine-using-the-wowza-ide)

Wowza Media Systems™ provides developers with a platform to create streaming applications and solutions. See [Wowza Developer Tools](https://www.wowza.com/developer) to learn more about our APIs and SDK.

To use the compiled version of the **CupertinoLiveAttachPicture** module, see [Add a poster image to an audio-only iOS stream with a Wowza Streaming Engine Java module](https://www.wowza.com/docs/how-to-add-poster-frames-to-apple-http-streams-id3-metadata-for-app-store-audio-renditions).

## Contact
[Wowza Media Systems, LLC](https://www.wowza.com/contact)

## License
This code is distributed under the [Wowza Public License](/LICENSE.txt).
