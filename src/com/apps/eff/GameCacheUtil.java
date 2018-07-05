package com.apps.eff;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apps.Constants;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.ParamUtils;
import com.framework.util.RandomUtils;
import com.game.model.GaBetOption;
import com.game.model.dto.GaDTO;
import com.game.service.IGaService;

/**
 * @author Mr.zang
 */
public class GameCacheUtil {
	public static Logger log = Logger.getLogger(GameCacheUtil.class);
/*	private final static IBaseDataService baseDataService = (IBaseDataService) ServiceLocatorImpl
			.getInstance().getService("baseDataService");*/

	private final static IGaService gaService = (IGaService) ServiceLocatorImpl
			.getInstance().getService("gaService");

	/**
	 * 根据游戏类型获得所有投注项
	 * 
	 * @param gameType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, GaBetOption> getLotteryBetOption(
			String gameType) {
		CacheManager manager;
		LinkedHashMap<String, GaBetOption> map = new LinkedHashMap<String, GaBetOption>();
		try {
			manager = CacheManager.create();
			Cache cache = manager.getCache("lotteryBetOption");
			Element element = cache.get(gameType);
			if (element != null) {
				map = (LinkedHashMap<String, GaBetOption>) element
						.getObjectValue();
			} else {
				if (ParamUtils.chkString(gameType)) {
					List<GaBetOption> list = gaService
							.findGaBetOptionByGameType(gameType);
					for (GaBetOption gaBetOption : list) {
						map.put(gaBetOption.getBetOptionId().toString(),
								gaBetOption);
					}
				}
				element = new Element(gameType, map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 更新投注项
	 * @param option
	 * @param gameType
	 */
	@SuppressWarnings("unchecked")
	public static void updateLotteryBetOption(GaBetOption option, String gameType) {
		CacheManager manager;
		LinkedHashMap<String, GaBetOption> map = new LinkedHashMap<String, GaBetOption>();
		try {
			manager = CacheManager.create();
			Cache cache = manager.getCache("lotteryBetOption");
			Element element = cache.get(gameType);
			if (element != null) {
				map = (LinkedHashMap<String, GaBetOption>) element
						.getObjectValue();
				map.put(option.getBetOptionId().toString(), option);
				element = new Element(gameType, map);
				cache.put(element);
			} else {
				if (ParamUtils.chkString(gameType)) {
					List<GaBetOption> list = gaService
							.findGaBetOptionByGameType(gameType);
					for (GaBetOption gaBetOption : list) {
						map.put(gaBetOption.getBetOptionId().toString(),
								gaBetOption);
					}
					map.put(option.getBetOptionId().toString(), option);
				}
				element = new Element(gameType, map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据游戏名字获得投注项
	 * 
	 * @param gameName
	 * @return
	 */
//	public static String getLotteryBetting(String gameName) {
//		CacheManager manager;
//		String betting = null;
//		try {
//			manager = CacheManager.create();
//			Cache cache = manager.getCache("lotteryBetting");
//			Element element = cache.get(gameName);
//			if (element != null) {
//				betting = (String) element.getObjectValue();
//			} else {
//				JSONArray betItems = new JSONArray();
//				String gameType = null;
//				String playType = null;
//				// ------------北京赛车------------
//				if ("bjpkLiangmianpan".equals(gameName)) {
//					gameType = "1";
//					playType = "0";
//				} else if ("bjpk110".equals(gameName)) {
//					gameType = "1";
//					playType = "1";
//				} else if ("bjpkGuanyajun".equals(gameName)) {
//					gameType = "1";
//					playType = "2";
//				}
//				// ------------北京赛车------------
//
//				
//				// ------------急速赛车------------
//				if ("sfpkLiangmianpan".equals(gameName)) {
//					gameType = "13";
//					playType = "0";
//				} else if ("sfpk110".equals(gameName)) {
//					gameType = "13";
//					playType = "1";
//				} else if ("sfpkGuanyajun".equals(gameName)) {
//					gameType = "13";
//					playType = "2";
//				}
//				// ------------急速赛车------------
//			
//				// ------------急速飞艇------------
//				if ("jsftmianpan".equals(gameName)) {
//					gameType = "14";
//					playType = "0";
//				} else if ("jsft".equals(gameName)) {
//					gameType = "14";
//					playType = "1";
//				} else if ("jsftGuanyajun".equals(gameName)) {
//					gameType = "14";
//					playType = "2";
//				}
//				// ------------急速飞艇------------
//				
//				// ------------北京三分彩------------
//				if ("bj3Liangmianpan".equals(gameName)) {
//					gameType = "0";
//					playType = "0";
//				} else if ("bj315".equals(gameName)) {
//					gameType = "0";
//					playType = "1";
//				}
//				// ------------北京三分彩------------
//
//				// ------------新加坡幸运28-----------
//				if ("xjp28Liangmianpan".equals(gameName)) {
//					gameType = "2";
//					playType = "0";
//				} else if ("xjp28Tema".equals(gameName)) {
//					gameType = "2";
//					playType = "1";
//				} else if ("xjp28shuzi".equals(gameName)) {
//					gameType = "2";
//					playType = "2";
//				}
//				// ------------新加坡幸运28------------
//
//				// ------------重庆时时彩-----------
//				if ("cqSscLiangmianpan".equals(gameName)) {
//					gameType = "3";
//					playType = "0";
//				} else if ("cqSsc15".equals(gameName)) {
//					gameType = "3";
//					playType = "1";
//				}
//				// ------------重庆时时彩------------
//
//				// ------------天津时时彩-----------
//				if ("tjSscLiangmianpan".equals(gameName)) {
//					gameType = "6";
//					playType = "0";
//				} else if ("tjSsc15".equals(gameName)) {
//					gameType = "6";
//					playType = "1";
//				}
//				// ------------天津时时彩------------
//
//				// ------------新疆时时彩-----------
//				if ("xjSscLiangmianpan".equals(gameName)) {
//					gameType = "7";
//					playType = "0";
//				} else if ("xjSsc15".equals(gameName)) {
//					gameType = "7";
//					playType = "1";
//				}
//				// ------------新疆时时彩------------
//
//				// ------------快乐扑克3-----------
//				if ("pokerLiangmianpan".equals(gameName)) {
//					gameType = "8";
//					playType = "0";
//				} else if ("pokerTema".equals(gameName)) {
//					gameType = "8";
//					playType = "1";
//				}
//				// ------------快乐扑克3------------
//
//				// ------------PC蛋蛋-----------
//				if ("bj28Liangmianpan".equals(gameName)) {
//					gameType = "4";
//					playType = "0";
//				} else if ("bj28Tema".equals(gameName)) {
//					gameType = "4";
//					playType = "1";
//				} else if ("bj28shuzi".equals(gameName)) {
//					gameType = "4";
//					playType = "2";
//				}
//				// ------------PC蛋蛋------------
//
//				// ------------广东快乐十分-----------
//				if ("gdk10Liangmianpan".equals(gameName)) {
//					gameType = "5";
//					playType = "0";
//				} else {
//					for (int i = 1; i < 9; i++) {
//						if (("gdk10di" + i + "qiu").equals(gameName)) {
//							gameType = "5";
//							playType = i + "";
//							break;
//						}
//					}
//				}
//				// ------------广东快乐十分------------
//
//				// ------------广东11选5-----------
//				if ("gdPick11Liangmianpan".equals(gameName)) {
//					gameType = "9";
//					playType = "0";
//				} else if ("gdPick15".equals(gameName)) {
//					gameType = "9";
//					playType = "1";
//				}
//				// ------------广东11选5------------
//
//				// ------------江苏快3-----------
//				if ("jsK3Sanjun".equals(gameName)) {
//					gameType = "10";
//					playType = "0";
//				} else if ("jsK3Dianshu".equals(gameName)) {
//					gameType = "10";
//					playType = "1";
//				}else if ("jsK3Changpai".equals(gameName)) {
//					gameType = "10";
//					playType = "2";
//				}
//				// ------------江苏快3------------
//
//				// ------------广东快乐十分-----------
//				if ("gxk10Liangmianpan".equals(gameName)) {
//					gameType = "11";
//					playType = "0";
//				} else {
//					for (int i = 1; i < 9; i++) {
//						if (("gxk10di" + i + "qiu").equals(gameName)) {
//							gameType = "11";
//							playType = i + "";
//							break;
//						}
//					}
//				}
//				// ------------广东快乐十分------------
//
//				// ------------三分时时彩-----------
//				if ("sfSscLiangmianpan".equals(gameName)) {
//					gameType = "15";
//					playType = "0";
//				} else if ("sfSsc15".equals(gameName)) {
//					gameType = "15";
//					playType = "1";
//				}
//				// ------------三分时时彩------------
//
//				// ------------香港六合彩-----------
//				for (int i = 0; i <= 51; i++) {
//					if (("markSix" + i).equals(gameName)) {
//						gameType = "12";
//						playType = i + "";
//					}
//				}
//				// ------------香港六合彩------------
//
//				if (ParamUtils.chkString(gameType)
//						&& ParamUtils.chkString(playType)) {
//					List<GaDTO> tlist = gaService.getOptionTypeList(gameType,
//							playType);
//					NumberFormat nf = NumberFormat.getInstance();
//					nf.setGroupingUsed(false);
//					if (tlist != null & tlist.size() > 0) {
//						for (int j = 0; j < tlist.size(); j++) {
//							List<GaBetOption> list = gaService
//									.getBetPanelOptionList(gameType, playType,
//											tlist.get(j).getOptionType()
//													.toString(), null);
//							JSONObject it = new JSONObject();
//							it.put("optionType", tlist.get(j).getOptionType()
//									.toString());
//							it.put("optionTitle", tlist.get(j).getTitle());
//							// 此处需要修改
//
//							if (list != null & list.size() > 0) {
//								JSONArray optionItems = new JSONArray();
//								for (int i = 0; i < list.size(); i++) {
//									JSONObject item = new JSONObject();
//									item.put("optionId", list.get(i)
//											.getBetOptionId());
//									item.put("title", list.get(i)
//											.getOptionTitle());
//									if (gameType.equals("12")) {
//										if (playType.equals("12")
//												|| playType.equals("15")) {
//											item.put("rate", list.get(i)
//													.getBetRate2());
//										} else {
//											item.put("rate", nf.format(list
//													.get(i).getBetRate()));
//										}
//									} else {
//										item.put("rate", nf.format(list.get(i)
//												.getBetRate()));
//									}
//									item.put("bettitle", tlist.get(j)
//											.getTitle());
//									optionItems.put(item);
//								}
//								it.put("optionItems", optionItems);
//								betItems.put(it);
//							} else {
//							}
//						}
//					}
//					if (ParamUtils.chkString(gameName)) {
//						element = new Element(gameName, betItems.toString());
//						cache.put(element);
//						betting = betItems.toString();
//					}
//				}
//			}
//		} catch (CacheException e) {
//			e.printStackTrace();
//		}
//		return betting;
//	}


	public static JSONArray getWinlist(String device, String ver)
			throws JSONException {
		JSONArray items = new JSONArray();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("winlist");
			Element element = cache.get("winlist");
			if (element != null) {
				String source = (String) element.getObjectValue();
				items = new JSONArray(source);
			} else {
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);

				String unit = "";
				if (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION())) {
					if (Constants.getIOS_REVIEW_VISION().indexOf("," + ver) > -1
							|| Constants.getIOS_REVIEW_VISION().indexOf(
									ver + ",") > -1
							|| Constants.getIOS_REVIEW_VISION().indexOf(ver) > -1) {
						unit = "元";
					} else {
						unit = "积分";
					}
				} else {
					unit = "积分";
				}
				for (int i = 0; i < 200; i++) {
					String gameName = "";
					int type = RandomUtils.getGameNext();
					if (type == 0) {
						gameName = "[三分彩]";
					} else if (type == 1) {
						gameName = "[北京赛车]";
					}
					 else if(type==2){
					 gameName="[幸运28]";
					 }
					else if (type == 3) {
						gameName = "[重庆时时彩]";
					} else if (type == 4) {
						gameName = "[PC蛋蛋]";
					} else if (type == 5) {
						gameName = "[广东快乐十分]";
					} 
					 else if(type==8){
					 gameName="[快乐扑克3]";
					 }
//					else if (type == 6) {
//						gameName = "[天津时时彩]";
//					} 
//				else if (type == 7) {
//						gameName = "[新疆时时彩]";
//					} 
				else if (type == 9) {
						gameName = "[广东11选5]";
					} else if (type == 10) {
						gameName = "[江苏快3]";
					} else if (type == 12) {
						gameName = "[六合彩]";
					}  else if (type == 13) {
						gameName = "[极速赛车]";
					}  else if (type == 14) {
						gameName = "[极速飞艇]";
					} else {
						continue;
					}

					String userId = "ID:" + RandomUtils.getUserID();
					BigDecimal money = RandomUtils.getMoney();
					items.put("恭喜 " + userId + " " + gameName + " 中奖"
							+ nf.format(money) + unit);
				}
				element = new Element("winlist", items.toString());
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getPlayName() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("playName");
			Element element = cache.get("playName");
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			} else {
				map.put("0", "特码A");
				map.put("1", "特码B");
				map.put("2", "正码");
				map.put("3", "正1特");
				map.put("4", "正2特");
				map.put("5", "正3特");
				map.put("6", "正4特");
				map.put("7", "正5特");
				map.put("8", "正6特");
				map.put("9", "正码1-6");
				map.put("10", "过关");
				map.put("11", "二全中");
				map.put("12", "二中特");
				map.put("13", "特串");
				map.put("14", "三全中");
				map.put("15", "三中二");
				map.put("16", "四全中");
				map.put("17", "半波");
				map.put("18", "一肖");
				map.put("19", "尾数");
				map.put("20", "特码生肖");
				map.put("21", "二肖");
				map.put("22", "三肖");
				map.put("23", "四肖");
				map.put("24", "五肖");
				map.put("25", "六肖");
				map.put("26", "七肖");
				map.put("27", "八肖");
				map.put("28", "九肖");
				map.put("29", "十肖");
				map.put("30", "十一肖");
				map.put("31", "二肖连中");
				map.put("32", "三肖连中");
				map.put("33", "四肖连中");
				map.put("34", "五肖连中");
				map.put("35", "二肖连不中");
				map.put("36", "三肖连不中");
				map.put("37", "四肖连不中");
				map.put("38", "二尾连中");
				map.put("39", "三尾连中");
				map.put("40", "四尾连中");
				map.put("41", "二尾连不中");
				map.put("42", "三尾连不中");
				map.put("43", "四尾连不中");
				map.put("44", "五不中");
				map.put("45", "六不中");
				map.put("46", "七不中");
				map.put("47", "八不中");
				map.put("48", "九不中");
				map.put("49", "十不中");
				map.put("50", "十一不中");
				map.put("51", "十二不中");
				element = new Element("playName", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return (HashMap<String, String>) map;
	}
		
//	/**
//	 * 获取开奖接口url
//	 */
//	@SuppressWarnings("unchecked")
//	public static HashMap<String, GaSessionInfo> getGameOpenUrl() {
//		Map<String, GaSessionInfo> map = new HashMap<String, GaSessionInfo>();
//		try {
//			CacheManager manager = CacheManager.create();
//			Cache cache = manager.getCache("gameOpenUrl");
//			Element element = cache.get("gameOpenUrl");
//			if (element != null) {
//				map = (Map<String, GaSessionInfo>) element.getObjectValue();
//			} else {
//				List<Object> para = new ArrayList<Object>();
//				StringBuffer hqls = new StringBuffer();
//				int startIndex=0;
//				int pageSize=999;
//				PaginationSupport ps=gaService.findGaSessionInfoList(hqls.toString(), para, startIndex, pageSize);
//				List<GaSessionInfo> list=ps.getItems();
//				if(list!=null&&list.size()>0){
//					for(GaSessionInfo gaSessionInfo:list){
//						if(gaSessionInfo.getGameType().equals("0")){
//							map.put("bj3", gaSessionInfo);//北京三分彩
//						}else if(gaSessionInfo.getGameType().equals("1")){
//							map.put("bjpk10", gaSessionInfo);//北京赛车 pk10
//						}else if(gaSessionInfo.getGameType().equals("2")){
//							map.put("xjplu28", gaSessionInfo);//幸运28 （新加坡幸运28）
//						}else if(gaSessionInfo.getGameType().equals("3")){
//							map.put("cqssc", gaSessionInfo);//重庆时时彩
//						}else if(gaSessionInfo.getGameType().equals("4")){
//							map.put("bjlu28", gaSessionInfo);//PC蛋蛋 （北京幸运28）
//						}else if(gaSessionInfo.getGameType().equals("5")){
//							map.put("gdk10", gaSessionInfo);//广东快乐十分
//						}else if(gaSessionInfo.getGameType().equals("6")){
//							map.put("tjssc", gaSessionInfo);//天津时时彩
//						}else if(gaSessionInfo.getGameType().equals("7")){
//							map.put("xjssc", gaSessionInfo);//新疆时时彩
//						}else if(gaSessionInfo.getGameType().equals("8")){
//							map.put("poker", gaSessionInfo);//快乐扑克3
//						}else if(gaSessionInfo.getGameType().equals("9")){
//							map.put("gdpick11", gaSessionInfo);//广东11选5
//						}else if(gaSessionInfo.getGameType().equals("10")){
//							map.put("jsk3", gaSessionInfo);//江苏快3
//						}else if(gaSessionInfo.getGameType().equals("11")){
//							map.put("gxk10", gaSessionInfo);//广西快乐十分
//						}else if(gaSessionInfo.getGameType().equals("12")){
//							map.put("marksix", gaSessionInfo);//六合彩
//						}else if(gaSessionInfo.getGameType().equals("13")){
//							map.put("sfpk10", gaSessionInfo);//极速赛车
//						}else if(gaSessionInfo.getGameType().equals("14")){
//							map.put("jsft", gaSessionInfo);//极速飞艇
//						}
//					}
//				}
//				element = new Element("gameOpenUrl", map);
//				cache.put(element);
//			}
//		} catch (CacheException e) {
//			e.printStackTrace();
//		}
//		return (HashMap<String, GaSessionInfo>) map;
//	}
//	
//	/**
//	 * 更新开奖接口url
//	 */
//	public static void updateGameOpenUrl() {
//		CacheManager manager = CacheManager.create();
//		Cache cache = manager.getCache("gameOpenUrl");
//		Map<String, GaSessionInfo> map = new HashMap<String, GaSessionInfo>();
//		Element element = cache.get("gameOpenUrl");	
//		List<Object> para = new ArrayList<Object>();
//		StringBuffer hqls = new StringBuffer();
//		int startIndex=0;
//		int pageSize=999;
//		PaginationSupport ps=gaService.findGaSessionInfoList(hqls.toString(), para, startIndex, pageSize);
//		@SuppressWarnings("unchecked")
//		List<GaSessionInfo> list=ps.getItems();
//		if(list!=null&&list.size()>0){
//			for(GaSessionInfo gaSessionInfo:list){
//				if(gaSessionInfo.getGameType().equals("0")){
//					map.put("bj3", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("1")){
//					map.put("bjpk10", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("2")){
//					map.put("xjplu28", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("3")){
//					map.put("cqssc", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("4")){
//					map.put("bjlu28", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("5")){
//					map.put("gdk10", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("6")){
//					map.put("tjssc", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("7")){
//					map.put("xjssc", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("8")){
//					map.put("poker", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("9")){
//					map.put("gdpick11", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("10")){
//					map.put("jsk3", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("11")){
//					map.put("gxk10", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("12")){
//					map.put("marksix", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("13")){
//					map.put("sfpk10", gaSessionInfo);
//				}else if(gaSessionInfo.getGameType().equals("14")){
//					map.put("jsft", gaSessionInfo);
//				}
//			}
//		}	
//		element = new Element("gameOpenUrl", map);
//		cache.put(element);
//	}
}
