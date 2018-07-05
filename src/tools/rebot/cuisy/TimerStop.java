package tools.rebot.cuisy;

import java.io.File;

import com.framework.util.ManageFile;

public class TimerStop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//stopTimer();
		startTimer();
	}

	public static void stopTimer(){
		//通过禁用spring配置文件来禁用定时任务，方便纯粹测试
		File[] configs = ManageFile.getFiles("D:/_Projects/_bxs_app/dadicaipiao/application/src/config");
		for(File f:configs){
			String data = ManageFile.loadTextFileUTF8(f.getAbsolutePath());
			if(data.contains("org.springframework.scheduling.quartz.SchedulerFactoryBean")){//配置有定时器的模块
				p(f.getAbsolutePath());
				//if(f.getAbsolutePath().contains("gaSfPk10")){
					data = data.replace("<list>", "<list><!--");
					data = data.replace("</list>", "--></list>");
					ManageFile.writeTextToFile(data, f.getAbsolutePath(), false);
				//}
				
			}
		}
	}
	
	public static void startTimer(){
		//通过禁用spring配置文件来禁用定时任务，方便纯粹测试
		File[] configs = ManageFile.getFiles("D:/_Projects/_bxs_app/dadicaipiao/application/src/config");
		for(File f:configs){
			String data = ManageFile.loadTextFileUTF8(f.getAbsolutePath());
			if(data.contains("org.springframework.scheduling.quartz.SchedulerFactoryBean")){//配置有定时器的模块
				p(f.getAbsolutePath());
				//if(f.getAbsolutePath().contains("gaSfPk10")){
					data = data.replace("<list><!--", "<list>");
					data = data.replace("--></list>", "</list>");
					ManageFile.writeTextToFile(data, f.getAbsolutePath(), false);
				//}
				
			}
		}
	}
	
	public static void p(Object obj){
		System.out.println(obj.toString());
	}
}
