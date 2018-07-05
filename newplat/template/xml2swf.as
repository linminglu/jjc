package {folderPath}
{
	import flash.display.Sprite;
	
	public class {ClassName} extends Sprite
	{
		[Embed(source='{folderPath}/{xmlName}.xml', mimeType="application/octet-stream")]
		public var ReadXML:Class;
	}
}