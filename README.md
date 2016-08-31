# wse-plugin-cupertinoliveattachpicture
The **ModuleCupertinoLiveAttachPicture** module for [Wowza Streaming Engine™ media server software](https://www.wowza.com/products/streaming-engine) enables you to add poster images to iOS audio-only live streams via the App Store compliance tehnique, live streams without compliance, and VOD streams.  

## Prerequisites
Wowza Streaming Engine 4.0.0 or later is required.

## Usage
With the App Store compliance configuration in place (CupertinoLiveAttachPicture enabled), you can test by using the following playback URL. Note the required ?wowzaaudioonly querystring. 

Poster images are added via the CupertinoLiveAttachPicture2 & CupertinoVODAttachPicture by adding ID3 metadata to the stream that's interpreted by the iOS player. The modules will search for an image file in the Wowza media server's [install-dir]/content folder that has the file name [stream-name].[extension], where [extension] can be jpg, jpeg, or png.  

## More resources
[Wowza Streaming Engine Server-Side API Reference](https://www.wowza.com/resources/WowzaStreamingEngine_ServerSideAPI.pdf)

[How to extend Wowza Streaming Engine using the Wowza IDE](https://www.wowza.com/forums/content.php?759-How-to-extend-Wowza-Streaming-Engine-using-the-Wowza-IDE)

Wowza Media Systems™ provides developers with a platform to create streaming applications and solutions. See [Wowza Developer Tools](https://www.wowza.com/resources/developers) to learn more about our APIs and SDK.

To use the compiled version of the Apple Compliance module, see [How to add poster frames to apple HTTP streams ID3 metadata for app store audio renditions (ModuleCupertinoLiveAttachPicture)](https://www.wowza.com/forums/content.php?207-How-to-add-poster-frames-to-Apple-HTTP-streams-ID3-metadata-for-App-Store-audio-renditions). 

To use the compiled version of the live/vod versions, see [How to add poster frames to apple HTTP streams ID3 metadata for on demand and live audio streams](https://www.wowza.com/forums/content.php?467-How-to-add-poster-frames-to-Apple-HTTP-streams-ID3-metadata-for-on-demand-and-live-audio-streams. 

## Contact
[Wowza Media Systems, LLC](https://www.wowza.com/contact)

## License
This code is distributed under the [Wowza Public License](https://github.com/WowzaMediaSystems/wse-plugin-cupertinoautomultibitratefilter/blob/master/LICENSE.txt).

![alt tag](http://wowzalogs.com/stats/githubimage.php?plugin=wse-plugin-cupertinoautomultibitratefilter)