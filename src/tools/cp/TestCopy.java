package tools.cp;

import com.framework.util.ManageFile;

public class TestCopy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String from = "D:/_Projects/_bxs_app/bxs.caipiao/kelecaipiao/application/src/com/pick11/gdpick11/web/action/GdPick11Action.java";
		String to = "D:/_Projects/_bxs_app/bxs.caipiao/kelecaipiao/application/src/tools/cp/tmp/a.java";
		
		String fData = ManageFile.loadTextFileUTF8(from);
		ManageFile.writeTextToFile(fData, to, false);
		//System.out.println(fData);
	}

}
