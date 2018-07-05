package com.apps.eff;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.apps.eff.dto.ValueConfig;
import com.apps.eff.dto.ValueItem;
import com.apps.model.City;
import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.Param;
import com.apps.service.IBaseDataService;
import com.apps.service.IParamService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.RandomUtils;
import com.framework.util.StringUtil;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.model.dto.GaDTO;
import com.game.service.IGaService;
import com.xy.hk.marksix.util.Lunar;

/**
 * 
 * 
 * @author Mr.zang
 * 
 */
public class CacheUtil {
	public static Logger log = Logger.getLogger(CacheUtil.class);
	private final static IBaseDataService baseDataService = (IBaseDataService) ServiceLocatorImpl
			.getInstance().getService("baseDataService");

	private final static IGaService gaService = (IGaService) ServiceLocatorImpl
			.getInstance().getService("gaService");
	private final static IParamService paramService = (IParamService) ServiceLocatorImpl
			.getInstance().getService("paramService");

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
	public static String getLotteryBetting(String gameName){
		return getLotteryBetting(gameName,false);
	}
	public static String getLotteryBetting(String gameName,boolean updateCache){
		CacheManager manager;
		String betting = null;
		try {
			manager = CacheManager.create();
			Cache cache = manager.getCache("lotteryBetting");
			Element element = cache.get(gameName);
			if (element != null && !updateCache) {
				betting = (String) element.getObjectValue();
			} else {
				JSONArray betItems = new JSONArray();
				String gameType = null;
				String playType = null;
				
				// ------------幸运飞艇------------
				if ("xyftLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XYFT;
					playType = "0";
				} else if ("xyft110".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XYFT;
					playType = "1";
				} else if ("xyftGuanyajun".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XYFT;
					playType = "2";
				}
				// ------------幸运飞艇------------
				
				
				// ------------重庆快乐十分-----------
				if ("cqk10Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQK10;
					playType = "0";
				} else if ("cqk10Lianma".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQK10;
					playType = "9";
				} else {
					for (int i = 1; i < 9; i++) {
						if (("cqk10di" + i + "qiu").equals(gameName)) {
							gameType = Constants.GAME_TYPE_XY_CQK10;
							playType = i + "";
							break;
						}
					}
				}
				// ------------重庆快乐十分------------
				
				
				// ------------五分彩------------
				if ("fiveLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "0";
				} else if ("five15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "1";
				} else if ("fiveQianerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "2";
				} else if ("fiveHouerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "3";
				} else if ("fiveQiansanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "4";
				} else if ("fiveZhongsanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "5";
				} else if ("fiveHousanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "6";
				} else if ("fiveQianerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "7";
				} else if ("fiveHouerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "8";
				} else if ("fiveQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "9";
				} else if ("fiveQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "10";
				} else if ("fiveHousanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "11";
				} else if ("fiveQiansanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "12";
				} else if ("fiveZhongsanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "13";
				} else if ("fiveHousanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "14";
				} else if ("fiveQiansan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "15";
				} else if ("fiveZhongsan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "16";
				} else if ("fiveHousan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_FC;
					playType = "17";
				}
				// ------------五分彩------------
				
				
				// ------------北京赛车------------
				if ("bjpkLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJPK10;
					playType = "0";
				} else if ("bjpk110".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJPK10;
					playType = "1";
				} else if ("bjpkGuanyajun".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJPK10;
					playType = "2";
				}
				// ------------北京赛车------------

				
				// ------------急速赛车------------
				if ("sfpkLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SFPK10;
					playType = "0";
				} else if ("sfpk110".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SFPK10;
					playType = "1";
				} else if ("sfpkGuanyajun".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SFPK10;
					playType = "2";
				}
				// ------------急速赛车------------
				
				// ------------北京三分彩------------
				if ("bj3Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "0";
				} else if ("bj315".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "1";
				}  else if ("bj3Qianerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "2";
				} else if ("bj3Houerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "3";
				} else if ("bj3Qiansanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "4";
				} else if ("bj3Zhongsanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "5";
				} else if ("bj3Housanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "6";
				} else if ("bj3Qianerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "7";
				} else if ("bj3Houerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "8";
				} else if ("bj3Qiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "9";
				} else if ("bj3Qiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "10";
				} else if ("bj3Housanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "11";
				} else if ("bj3Qiansanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "12";
				} else if ("bj3Zhongsanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "13";
				} else if ("bj3Housanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "14";
				} else if ("bj3Qiansan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "15";
				} else if ("bj3Zhongsan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "16";
				} else if ("bj3Housan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJ3;
					playType = "17";
				}
				// ------------北京三分彩------------

				// ------------新加坡幸运28-----------
				if ("xjp28Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJPLU28;
					playType = "0";
				} else if ("xjp28Tema".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJPLU28;
					playType = "1";
				}
				// ------------新加坡幸运28------------

				// ------------重庆时时彩-----------
				if ("cqSscLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "0";
				} else if ("cqSsc15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "1";
				} else if ("cqSscQianerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "2";
				} else if ("cqSscHouerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "3";
				} else if ("cqSscQiansanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "4";
				} else if ("cqSscZhongsanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "5";
				} else if ("cqSscHousanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "6";
				} else if ("cqSscQianerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "7";
				} else if ("cqSscHouerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "8";
				} else if ("cqSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "9";
				} else if ("cqSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "10";
				} else if ("cqSscHousanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "11";
				} else if ("cqSscQiansanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "12";
				} else if ("cqSscZhongsanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "13";
				} else if ("cqSscHousanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "14";
				} else if ("cqSscQiansan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "15";
				} else if ("cqSscZhongsan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "16";
				} else if ("cqSscHousan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_CQSSC;
					playType = "17";
				}
				// ------------重庆时时彩------------

				// ------------新疆时时彩-----------
				if ("xjSscLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "0";
				} else if ("xjSsc15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "1";
				} else if ("xjSscQianerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "2";
				} else if ("xjSscHouerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "3";
				} else if ("xjSscQiansanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "4";
				} else if ("xjSscZhongsanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "5";
				} else if ("xjSscHousanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "6";
				} else if ("xjSscQianerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "7";
				} else if ("xjSscHouerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "8";
				} else if ("xjSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "9";
				} else if ("xjSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "10";
				} else if ("xjSscHousanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "11";
				} else if ("xjSscQiansanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "12";
				} else if ("xjSscZhongsanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "13";
				} else if ("xjSscHousanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "14";
				} else if ("xjSscQiansan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "15";
				} else if ("xjSscZhongsan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "16";
				} else if ("xjSscHousan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "17";
				}
				// ------------新疆时时彩------------
				
				// ------------天津时时彩-----------
				if ("tjSscLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "0";
				} else if ("tjSsc15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "1";
				} else if ("tjSscQianerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "2";
				} else if ("tjSscHouerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "3";
				} else if ("tjSscQiansanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "4";
				} else if ("tjSscZhongsanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "5";
				} else if ("tjSscHousanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "6";
				} else if ("tjSscQianerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "7";
				} else if ("tjSscHouerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "8";
				} else if ("tjSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "9";
				} else if ("tjSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "10";
				} else if ("tjSscHousanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "11";
				} else if ("tjSscQiansanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "12";
				} else if ("tjSscZhongsanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "13";
				} else if ("tjSscHousanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "14";
				} else if ("tjSscQiansan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "15";
				} else if ("tjSscZhongsan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "16";
				} else if ("tjSscHousan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "17";
				}
				// ------------天津时时彩------------
				
				// ------------北京时时彩------------
				if ("bjSscLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "0";
				} else if ("bjSsc15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "1";
				} else if ("bjSscQianerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "2";
				} else if ("bjSscHouerzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "3";
				} else if ("bjSscQiansanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "4";
				} else if ("bjSscZhongsanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "5";
				} else if ("bjSscHousanzhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "6";
				} else if ("bjSscQianerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "7";
				} else if ("bjSscHouerzuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "8";
				} else if ("bjSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "9";
				} else if ("bjSscQiansanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "10";
				} else if ("bjSscHousanzusan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "11";
				} else if ("bjSscQiansanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "12";
				} else if ("bjSscZhongsanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "13";
				} else if ("bjSscHousanzuliu".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "14";
				} else if ("bjSscQiansan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "15";
				} else if ("bjSscZhongsan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "16";
				} else if ("bjSscHousan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJSSC;
					playType = "17";
				}
				// ------------北京时时彩------------
				
				// ------------天津时时彩-----------
				if ("tjSscLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "0";
				} else if ("tjSsc15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_TJSSC;
					playType = "1";
				}
				// ------------天津时时彩------------

				// ------------新疆时时彩-----------
				if ("xjSscLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "0";
				} else if ("xjSsc15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_XJSSC;
					playType = "1";
				}
				// ------------新疆时时彩------------

				// ------------快乐扑克3-----------
				if ("pokerLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_POKER;
					playType = "0";
				} else if ("pokerTema".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_POKER;
					playType = "1";
				}
				// ------------快乐扑克3------------

				// ------------PC蛋蛋-----------
				if ("bj28Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJLU28;
					playType = "0";
				} else if ("bj28Tema".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJLU28;
					playType = "1";
				}
				// ------------PC蛋蛋------------

				// ------------广东快乐十分-----------
				if ("gdk10Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDK10;
					playType = "0";
				} else if ("gdk10Lianma".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDK10;
					playType = "9";
				} else {
					for (int i = 1; i < 9; i++) {
						if (("gdk10di" + i + "qiu").equals(gameName)) {
							gameType = Constants.GAME_TYPE_XY_GDK10;
							playType = i + "";
							break;
						}
					}
				}
				// ------------广东快乐十分------------

				// ------------广东11选5-----------
				if ("gdPick11Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDPICK11;
					playType = "0";
				} else if ("gdPick15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDPICK11;
					playType = "1";
				}else if ("gdPick11Renxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDPICK11;
					playType = "2";
				}else if ("gdPick11Zuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDPICK11;
					playType = "3";
				}else if ("gdPick11Zhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GDPICK11;
					playType = "4";
				}
				// ------------广东11选5------------
				
				// ------------江西11选5------------
				if ("jxPick11Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JXPICK11;
					playType = "0";
				} else if ("jxPick15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JXPICK11;
					playType = "1";
				}else if ("jxPick11Renxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JXPICK11;
					playType = "2";
				}else if ("jxPick11Zuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JXPICK11;
					playType = "3";
				}else if ("jxPick11Zhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JXPICK11;
					playType = "4";
				}
				// ------------江西11选5------------
				
				
				// ------------山东11选5-----------
				if ("sdPick11Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SDPICK11;
					playType = "0";
				} else if ("sdPick15".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SDPICK11;
					playType = "1";
				}else if ("sdPick11Renxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SDPICK11;
					playType = "2";
				}else if ("sdPick11Zuxuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SDPICK11;
					playType = "3";
				}else if ("sdPick11Zhixuan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SDPICK11;
					playType = "4";
				}
				// ------------山东11选5------------

				// ------------江苏快3-----------
				if ("jsK3Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JSK3;
					playType = "0";
				} else if ("jsK3Lianglian".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JSK3;
					playType = "1";
				}
				// ------------江苏快3------------
				
				// ------------北京快3-----------
				if ("bjK3Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJK3;
					playType = "0";
				} else if ("bjK3Lianglian".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_BJK3;
					playType = "1";
				}
				// ------------北京快3------------

				// ------------广西快乐十分-----------
				if ("gxk10Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GXK10;
					playType = "0";
				}else if ("gxk10Lianma".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_GXK10;
					playType = "9";
				}  else {
					for (int i = 1; i < 9; i++) {
						if (("gxk10di" + i + "qiu").equals(gameName)) {
							gameType = Constants.GAME_TYPE_XY_GXK10;
							playType = i + "";
							break;
						}
					}
				}
				// ------------广东快乐十分------------

				// ------------香港六合彩-----------
				for (int i = 0; i <= 51; i++) {
					if (("markSix" + i).equals(gameName)) {
						gameType = Constants.GAME_TYPE_XY_MARKSIX;
						playType = i + "";
					}
				}
				// ------------香港六合彩------------
				
				// ------------急速六合彩-----------
				for (int i = 0; i <= 51; i++) {
					if (("sflhc" + i).equals(gameName)) {
						gameType = Constants.GAME_TYPE_XY_SFLHC;
						playType = i + "";
					}
				}
				// ------------急速六合彩------------
				// ------------三分pk10------------
				if ("sfpk2Liangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SFPK102;
					playType = "0";
				} else if ("sfpk2110".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SFPK102;
					playType = "1";
				} else if ("sfpk2Guanyajun".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_SFPK102;
					playType = "2";
				}
				// ------------三分pk10------------
				// ------------急速飞艇------------
				if ("jsftLiangmianpan".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JSFT;
					playType = "0";
				} else if ("jsft110".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JSFT;
					playType = "1";
				} else if ("jsftGuanyajun".equals(gameName)) {
					gameType = Constants.GAME_TYPE_XY_JSFT;
					playType = "2";
				}
				// ------------急速赛车------------

				if (ParamUtils.chkString(gameType)
						&& ParamUtils.chkString(playType)) {
					List<GaDTO> tlist = gaService.getOptionTypeList(gameType,
							playType);
					NumberFormat nf = NumberFormat.getInstance();
					nf.setGroupingUsed(false);
					if (tlist != null & tlist.size() > 0) {
						for (int j = 0; j < tlist.size(); j++) {
							List<GaBetOption> list = gaService
									.getBetPanelOptionList(gameType, playType,
											tlist.get(j).getOptionType()
													.toString(), null);
							JSONObject it = new JSONObject();
							it.put("optionType", tlist.get(j).getOptionType()
									.toString());
							it.put("optionTitle", tlist.get(j).getTitle());
							// 此处需要修改

							if (list != null & list.size() > 0) {
								JSONArray optionItems = new JSONArray();
								for (int i = 0; i < list.size(); i++) {
									JSONObject item = new JSONObject();
									item.put("optionId", list.get(i)
											.getBetOptionId());
									item.put("title", list.get(i)
											.getOptionTitle());
									if (Constants.GAME_TYPE_XY_MARKSIX.equals(gameType) || Constants.GAME_TYPE_XY_SFLHC.equals(gameType)) {
										if (playType.equals("12")
												|| playType.equals("15")) {
											item.put("rate", list.get(i)
													.getBetRate2());
										} else {
											item.put("rate", nf.format(list
													.get(i).getBetRate()));
										}
									} else {
										item.put("rate", nf.format(list.get(i)
												.getBetRate()));
									}
									item.put("bettitle", tlist.get(j)
											.getTitle());
									optionItems.put(item);
								}
								it.put("optionItems", optionItems);
								betItems.put(it);
							} else {
							}
						}
					}
					if (ParamUtils.chkString(gameName)) {
						element = new Element(gameName, betItems.toString());
						cache.put(element);
						betting = betItems.toString();
					}
				}
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return betting;
	}

	public static JSONArray getCity3() throws JSONException {
		JSONArray items = new JSONArray();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("cityList3");
			Element element = cache.get("cityList3");
			if (element != null) {
				String source = (String) element.getObjectValue();
				items = new JSONArray(source);
			} else {
				List<City> delList = new ArrayList<City>();
				List<City> shengList = baseDataService
						.findCity(Constants.CITY_SHENG);
				List<City> shiList = baseDataService
						.findCity(Constants.CITY_SHI);
				// List<City> quList =
				// baseDataService.findCity(Constants.CITY_QU);
				for (City sheng : shengList) {
					String title = sheng.getTitle();
					if (title.contains("台湾") || title.contains("香港")
							|| title.contains("澳门")) {
						delList.add(sheng);
					}
				}

				if (delList.size() > 0) {
					shengList.removeAll(delList);
				}

				for (City sheng : shengList) {
					Integer cid = sheng.getCid();
					String title = sheng.getTitle();
					JSONObject obj = new JSONObject();
					obj.put("cid", cid);
					obj.put("title", title);
					JSONArray list = new JSONArray();
					for (City shi : shiList) {
						Integer parentId2 = shi.getParentId();
						if (parentId2.intValue() == cid.intValue()) {
							JSONObject obj2 = new JSONObject();
							Integer cid2 = shi.getCid();
							String title2 = shi.getTitle();
							obj2.put("cid", cid2);
							obj2.put("title", title2);
							List<City> qList = baseDataService
									.findAreaByCityId(cid2);
							JSONArray list2 = new JSONArray();
							for (City qu : qList) {
								JSONObject obj3 = new JSONObject();
								Integer cid3 = qu.getCid();
								String title3 = qu.getTitle();
								obj3.put("cid", cid3);
								obj3.put("title", title3);
								list2.put(obj3);
							}
							obj2.put("list", list2);
							list.put(obj2);
						}
					}
					obj.put("list", list);
					items.put(obj);
				}
				element = new Element("cityList3", items.toString());
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return items;
	}

	public static JSONArray getWechatCity3() throws JSONException {
		JSONArray items = new JSONArray();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("cityList3wechat");
			Element element = cache.get("cityList3wechat");
			if (element != null) {
				String source = (String) element.getObjectValue();
				items = new JSONArray(source);
			} else {
				List<City> delList = new ArrayList<City>();
				List<City> shengList = baseDataService
						.findCity(Constants.CITY_SHENG);
				List<City> shiList = baseDataService
						.findCity(Constants.CITY_SHI);
				// List<City> quList =
				// baseDataService.findCity(Constants.CITY_QU);
				for (City sheng : shengList) {
					String title = sheng.getTitle();
					if (title.contains("台湾") || title.contains("香港")
							|| title.contains("澳门")) {
						delList.add(sheng);
					}
				}

				if (delList.size() > 0) {
					shengList.removeAll(delList);
				}

				for (City sheng : shengList) {
					Integer cid = sheng.getCid();
					String title = sheng.getTitle();
					JSONObject obj = new JSONObject();
					obj.put("value", cid);
					obj.put("text", title);
					JSONArray list = new JSONArray();
					for (City shi : shiList) {
						Integer parentId2 = shi.getParentId();
						if (parentId2.intValue() == cid.intValue()) {
							JSONObject obj2 = new JSONObject();
							Integer cid2 = shi.getCid();
							String title2 = shi.getTitle();
							obj2.put("value", cid2);
							obj2.put("text", title2);
							List<City> qList = baseDataService
									.findAreaByCityId(cid2);
							JSONArray list2 = new JSONArray();
							for (City qu : qList) {
								JSONObject obj3 = new JSONObject();
								Integer cid3 = qu.getCid();
								String title3 = qu.getTitle();
								obj3.put("value", cid3);
								obj3.put("text", title3);
								list2.put(obj3);
							}
							obj2.put("children", list2);
							list.put(obj2);
						}
					}
					obj.put("children", list);
					items.put(obj);
				}
				element = new Element("cityList3wechat", items.toString());
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return items;
	}

	public static JSONArray getWinlist_bak(String device, String ver)
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
						unit = "积分";
					} else {
						unit = "积分";
					}
				} else {
					unit = "积分";
				}
				for (int i = 0; i < 200; i++) {
					String gameName = "";
					int type = RandomUtils.getGameNext();
					JSONObject it = new JSONObject();
					if (type == 14) {
						gameName = "[三分彩]";
					}else if (type == 3) {
						gameName = "[重庆时时彩]";
					} else if (type == 9) {
						gameName = "[广东11选5]";
					} else if(type == 15){
						gameName = "[五分彩]";
					}else{
						continue;
					}

					String userId = "ID:" + RandomUtils.getUserID();
					BigDecimal money = RandomUtils.getMoney();
					it.put("userName", userId);
					it.put("gameName", gameName);
					it.put("money", money);
					items.put(it);
				}
				element = new Element("winlist", items.toString());
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return items;
	}
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
						unit = "积分";
					} else {
						unit = "积分";
					}
				} else {
					unit = "积分";
				}
				for (int i = 0; i < 200; i++) {
					String gameName = RandomUtils.getGameTitle();
//					int type = RandomUtils.getGameNext();
//					if (type == 0) {
//						gameName = "[重庆时时彩]";
//					} else if (type == 1) {
//						gameName = "[天津时时彩]";
//					}else if(type==2){
//						gameName="[新疆时时彩]";
//					}else if (type == 31) {
//						gameName = "[重庆时时彩]";
//					} else if (type == 4) {
//						gameName = "[PC蛋蛋]";
//					} else if (type == 5) {
//						gameName = "[广东快乐十分]";
//					}
//					// else if(type==8){
//					// gameName="[快乐扑克3]";
//					// }
//					else if (type == 6) {
//						gameName = "[天津时时彩]";
//					} else if (type == 7) {
//						gameName = "[新疆时时彩]";
//					} else if (type == 9) {
//						gameName = "[广东11选5]";
//					} else if (type == 10) {
//						gameName = "[江苏快3]";
//					} else if (type == 12) {
//						gameName = "[六合彩]";
//					} else {
//						continue;
//					}
		
					//String userId = "ID:" + RandomUtils.getUserID();
					String userId = "ID:***" + StringUtil.getRandomNumber(2);
					BigDecimal money = RandomUtils.getMoney();
					items.put("恭喜 " + userId + " " + gameName + " 中奖"+ nf.format(money) + unit);
				}
				element = new Element("winlist", items.toString());
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return items;
		}

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

	public static Param getParam(String type) {
		Map<String, Param> map = new HashMap<String, Param>();
		Param param = null;
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("paramList");
			Element element = cache.get("params");
			
			StringBuffer logs = new StringBuffer();
			logs.append("___get param ["+type+"] by ");
			
			if (element != null) {
				map = (Map<String, Param>) element.getObjectValue();
				param = map.get(type);
				logs.append(" cache val="+(param!=null?param.getValue():"null"));
			} else {
				List<Param> list = paramService.findParamList();
				for (Param temp : list) {
					map.put(temp.getType(), temp);
				}
				element = new Element("params", map);
				cache.put(element);
				param = map.get(type);
				logs.append(" SQL val="+(param!=null?param.getValue():"null"));
			}
			
			GameHelpUtil.log(logs.toString());
			
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return param;
	}
	
	/**
	 * 获取缓存配置值
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getParamValue(String type) {
		Map<String, Param> map = new HashMap<String, Param>();
		Param param = null;
		
		StringBuffer logs = new StringBuffer();
		logs.append("___get param value ["+type+"] by ");
		
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("paramList");
			Element element = cache.get("params");
			
			if (element != null) {
				map = (Map<String, Param>) element.getObjectValue();
				param = map.get(type);
				
				logs.append("cache val="+(param!=null?param.getValue():"null"));
				
			} else {
				List<Param> list = paramService.findParamList();
				for (Param temp : list) {
					map.put(temp.getType(), temp);
				}
				element = new Element("params", map);
				cache.put(element);
				param = map.get(type);
				
				logs.append("SQL val="+(param!=null?param.getValue():"null"));
			}
			
		} catch (CacheException e) {
			logs.append(" error="+e.getMessage());
			//e.printStackTrace();
		}
		
		GameHelpUtil.log(logs.toString());
		return param!=null && ParamUtils.chkString(param.getValue())?param.getValue():null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Param> getParam() {
		List<Param> list = new ArrayList<Param>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("paramList");
			Element element = cache.get("paramList");
			if (element != null) {
				list = (List<Param>) element.getObjectValue();
			} else {
				list = paramService.findParamList();
				element = new Element("paramList", list);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static void delParam() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("paramList");
		if (cache.get("paramList") != null) {
			cache.remove("paramList");
		}
	}
	
	/**
	 * 更新param
	 * 
	 * @return
	 */
	public static void updateParam() {
		List<Param> list = new ArrayList<Param>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("paramList");
			Element element = cache.get("paramList");

			list = paramService.findParamList();
			element = new Element("paramList", list);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, GaSessionInfo> getGameOpenUrl() {
		Map<String, GaSessionInfo> map = new HashMap<String, GaSessionInfo>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameOpenUrl");
			Element element = cache.get("gameOpenUrl");
			if (element != null) {
				map = (Map<String, GaSessionInfo>) element.getObjectValue();
			} else {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				int startIndex=0;
				int pageSize=999;
				PaginationSupport ps=gaService.findGaSessionInfoList(hqls.toString(), para, startIndex, pageSize);
				List<GaSessionInfo> list=ps.getItems();
				if(list!=null&&list.size()>0){
					String gfxy = "";
					for(GaSessionInfo gaSessionInfo:list){
						
						gfxy = Constants.getGFXYPre(gaSessionInfo.getPlayCate());
						//map.put(gfxy+gaSessionInfo.getGameCode(), gaSessionInfo);
						map.put(gaSessionInfo.getGameType(), gaSessionInfo);//用gameType保存map
						
//						if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_CQSSC)){
//							map.put("xycqssc", gaSessionInfo);
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_TJSSC)){
//							map.put("xytjssc", gaSessionInfo);
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJSSC)){
//							map.put("xyxjssc", gaSessionInfo);
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSK3)){
//							map.put("xyjsk3", gaSessionInfo);
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GDPICK11)){
//							map.put("xygdpick11", gaSessionInfo);
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJPK10)){
//							map.put("xybjpk10", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFPK10)){
//							map.put("xysfpk10", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSFT)){
//							map.put("xyjsft", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFPK102)){
//							map.put("xysfpk102", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GDK10)){
//							map.put("xygdk10", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GXK10)){
//							map.put("xygxk10", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJPLU28)){
//							map.put("xyxjplu28", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJLU28)){
//							map.put("xybjlu28", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_POKER)){
//							map.put("xypoker", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJ3)){
//							map.put("xybj3", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_MARKSIX)){
//							map.put("xymarksix", gaSessionInfo);//
//						}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFLHC)){
//							map.put("xysflhc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_CQSSC)) {
//							map.put("gfcqssc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_TJSSC)) {
//							map.put("gftjssc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_XJSSC)) {
//							map.put("gfxjssc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JXSSC)) {
//							map.put("gfjxssc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JSK3)) {
//							map.put("gfjsk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_AHK3)) {
//							map.put("gfahk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_BJK3)) {
//							map.put("gfbjk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_FJK3)) {
//							map.put("gffjk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GZK3)) {
//							map.put("gfgzk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GXK3)) {
//							map.put("gfgxk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GSK3)) {
//							map.put("gfgsk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HUBK3)) {
//							map.put("gfhubk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HEBK3)) {
//							map.put("gfhebk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HNK3)) {
//							map.put("gfhnk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JXK3)) {
//							map.put("gfjxk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JLK3)) {
//							map.put("gfjlk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_NMGK3)) {
//							map.put("gfnmgk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_SHK3)) {
//							map.put("gfshk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GDPICK11)) {
//							map.put("gfgdpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JXPICK11)) {
//							map.put("gfjxpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_SDPICK11)) {
//							map.put("gfsdpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_SXPICK11)) {
//							map.put("gfsxpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_BJPICK11)) {
//							map.put("gfbjpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_TJPICK11)) {
//							map.put("gftjpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HEBPICK11)) {
//							map.put("gfhebpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_NMGPICK11)) {
//							map.put("gfnmgpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_LNPICK11)) {
//							map.put("gflnpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JLPICK11)) {
//							map.put("gfjlpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HLJPICK11)) {
//							map.put("gfhljpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_SHPICK11)) {
//							map.put("gfshpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_JSPICK11)) {
//							map.put("gfjspick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_ZJPICK11)) {
//							map.put("gfzjpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_AHPICK11)) {
//							map.put("gfahpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_FJPICK11)) {
//							map.put("gffjpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HNPICK11)) {
//							map.put("gfhnpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_HUBPICK11)) {
//							map.put("gfhubpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GXPICK11)) {
//							map.put("gfgxpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_SFPK102)) {
//							map.put("gfscpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GZPICK11)) {
//							map.put("gfgzpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_SHXPICK11)) {
//							map.put("gfshxpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GSPICK11)) {
//							map.put("gfgspick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_XJPICK11)) {
//							map.put("gfxjpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_YNPICK11)) {
//							map.put("gfynpick11", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_BJPK10)) {
//							map.put("gfbjpk10", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_BJ3)) {
//							map.put("gfbj3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_FC)) {
//							map.put("gffc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_THREE)) {
//							map.put("gfthree", gaSessionInfo);
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_DCB)) {
//							map.put("gfdcb", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_BJKL8)) {
//							map.put("gfbjkl8", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_BJLU28)) {
//							map.put("gfpcegg", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_XJPLU28)) {
//							map.put("gfxjp28", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_POKER)) {
//							map.put("gfklpk3", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_MARKSIX)) {
//							map.put("gflhc", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GDK10)) {
//							map.put("gfgdk10", gaSessionInfo);// 
//						} else if (gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_GF_GXK10)) {
//							map.put("gfgxk10", gaSessionInfo);// 
//						}
					}
				}
				element = new Element("gameOpenUrl", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return (HashMap<String, GaSessionInfo>) map;
	}
	
	public static void updateGameOpenUrl() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("gameOpenUrl");
		Map<String, GaSessionInfo> map = new HashMap<String, GaSessionInfo>();
		Element element = cache.get("gameOpenUrl");	
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		int startIndex=0;
		int pageSize=999;
		PaginationSupport ps=gaService.findGaSessionInfoList(hqls.toString(), para, startIndex, pageSize);
		List<GaSessionInfo> list=ps.getItems();
		if(list!=null&&list.size()>0){
			for(GaSessionInfo gaSessionInfo:list){
				if(gaSessionInfo.getGameType().equals("0")){
					map.put("bj3", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("1")){
					map.put("bjpk10", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("2")){
					map.put("xjplu28", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("3")){
					map.put("cqssc", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("4")){
					map.put("bjlu28", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("5")){
					map.put("gdk10", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("6")){
					map.put("tjssc", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("7")){
					map.put("xjssc", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("8")){
					map.put("poker", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("9")){
					map.put("gdpick11", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("10")){
					map.put("jsk3", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("11")){
					map.put("gxk10", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("12")){
					map.put("marksix", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("13")){
					map.put("sfpk10", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("14")){
					map.put("sfpk102", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("15")){
					map.put("jsft", gaSessionInfo);
				}else if(gaSessionInfo.getGameType().equals("16")){
					map.put("sflhc", gaSessionInfo);
				}
//				else if(Constants.GAME_TYPE_JISUFT.equals(gaSessionInfo.getGameType())){
//					map.put("jisuft", gaSessionInfo);//幸运飞艇
//				}else if(Constants.GAME_TYPE_FIVE.equals(gaSessionInfo.getGameType())){
//					map.put("five", gaSessionInfo);//五分彩
//				}else if(Constants.GAME_TYPE_BJSSC.equals(gaSessionInfo.getGameType())){
//					map.put("bjssc", gaSessionInfo);//北京时时彩
//				}else if(Constants.GAME_TYPE_CQK10.equals(gaSessionInfo.getGameType())){
//					map.put("cqk10", gaSessionInfo);//重庆快乐十分
//				}else if(Constants.GAME_TYPE_JXPICK11.equals(gaSessionInfo.getGameType())){
//					map.put("jxpick11", gaSessionInfo);//江西11选5
//				}

			}
		}	
		element = new Element("gameOpenUrl", map);
		cache.put(element);
	}
	
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getMarkSixMap(String luyear,Date startTime) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("markSixMap");
			Element element = cache.get(luyear);
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			} else {
				String ji = "01,13,25,37,49";// 鸡 2017
				String shu = "10,22,34,46";// 鼠 2017
				String hu = "08,20,32,44";// 虎 2017
				String lon = "06,18,30,42";// 龙 2017
				String ma = "04,16,28,40";// 马 2017
				String hou = "02,14,26,38";// 猴 2017
				String gou = "12,24,36,48";// 狗 2017
				String niu = "09,21,33,45";// 牛 2017
				String tu = "07,19,31,43";// 兔 2017
				String she = "05,17,29,41";// 蛇 2017
				String yang = "03,15,27,39";// 羊 2017
				String zhu = "11,23,35,47";// 猪 2017
				
				int year2 = DateTimeUtil.getRightYear(startTime);
				int month = DateTimeUtil.getRightMonth(startTime)-1;
				int day = DateTimeUtil.getRightDay(startTime);
			    Calendar cal=Calendar.getInstance();
			    cal.set(year2, month, day);
			    Lunar lunar=new Lunar(cal);
			    int year = lunar.year;
				if(ParamUtils.chkInteger(year)){
		            if(((year-4)%12)==0){
		        		shu = "01,13,25,37,49";
		        		zhu = "02,14,26,38";
		        		gou = "03,15,27,39";
		        		ji = "04,16,28,40";
		        		hou = "05,17,29,41";
		        		yang = "06,18,30,42";
		        		ma = "07,19,31,43";
		        		she = "08,20,32,44";
		        		lon = "09,21,33,45";
		        		tu = "10,22,34,46";
		        		hu = "11,23,35,47";
		        		niu = "12,24,36,48";            
		        	}else if(((year-4)%12)==1){
		        		shu = "02,14,26,38";
		        		zhu = "03,15,27,39";
		        		gou = "04,16,28,40";
		        		ji = "05,17,29,41";
		        		hou = "06,18,30,42";
		        		yang = "07,19,31,43";
		        		ma = "08,20,32,44";
		        		she = "09,21,33,45";
		        		lon = "10,22,34,46";
		        		tu = "11,23,35,47";
		        		hu = "12,24,36,48";
		        		niu = "01,13,25,37,49";
		            }else if(((year-4)%12)==2){
		        		shu = "03,15,27,39";
		        		zhu = "04,16,28,40";
		        		gou = "05,17,29,41";
		        		ji = "06,18,30,42";
		        		hou = "07,19,31,43";
		        		yang = "08,20,32,44";
		        		ma = "09,21,33,45";
		        		she = "10,22,34,46";
		        		lon = "11,23,35,47";
		        		tu = "12,24,36,48";
		        		hu = "01,13,25,37,49";
		        		niu = "02,14,26,38";
		            }else if(((year-4)%12)==3){
		        		shu = "04,16,28,40";
		        		zhu = "05,17,29,41";
		        		gou = "06,18,30,42";
		        		ji = "07,19,31,43";
		        		hou = "08,20,32,44";
		        		yang = "09,21,33,45";
		        		ma = "10,22,34,46";
		        		she = "11,23,35,47";
		        		lon = "12,24,36,48";
		        		tu = "01,13,25,37,49";
		        		hu = "02,14,26,38";
		        		niu = "03,15,27,39";
		            }else if(((year-4)%12)==4){
		        		shu = "05,17,29,41";
		        		zhu = "06,18,30,42";
		        		gou = "07,19,31,43";
		        		ji = "08,20,32,44";
		        		hou = "09,21,33,45";
		        		yang = "10,22,34,46";
		        		ma = "11,23,35,47";
		        		she = "12,24,36,48";
		        		lon = "01,13,25,37,49";
		        		tu = "02,14,26,38";
		        		hu = "03,15,27,39";
		        		niu = "04,16,28,40";
		            }else if(((year-4)%12)==5){
		        		shu = "06,18,30,42";
		        		zhu = "07,19,31,43";
		        		gou = "08,20,32,44";
		        		ji = "09,21,33,45";
		        		hou = "10,22,34,46";
		        		yang = "11,23,35,47";
		        		ma = "12,24,36,48";
		        		she = "01,13,25,37,49";
		        		lon = "02,14,26,38";
		        		tu = "03,15,27,39";
		        		hu = "04,16,28,40";
		        		niu = "05,17,29,41";
		            }else if(((year-4)%12)==6){
		        		shu = "07,19,31,43";
		        		zhu = "08,20,32,44";
		        		gou = "09,21,33,45";
		        		ji = "10,22,34,46";
		        		hou = "11,23,35,47";
		        		yang = "12,24,36,48";
		        		ma = "01,13,25,37,49";
		        		she = "02,14,26,38";
		        		lon = "03,15,27,39";
		        		tu = "04,16,28,40";
		        		hu = "05,17,29,41";
		        		niu = "06,18,30,42";
		            }else if(((year-4)%12)==7){
		        		shu = "08,20,32,44";
		        		zhu = "09,21,33,45";
		        		gou = "10,22,34,46";
		        		ji = "11,23,35,47";
		        		hou = "12,24,36,48";
		        		yang = "01,13,25,37,49";
		        		ma = "02,14,26,38";
		        		she = "03,15,27,39";
		        		lon = "04,16,28,40";
		        		tu = "05,17,29,41";
		        		hu = "06,18,30,42";
		        		niu = "07,19,31,43";
		            }else if(((year-4)%12)==8){
		        		shu = "09,21,33,45";
		        		zhu = "10,22,34,46";
		        		gou = "11,23,35,47";
		        		ji = "12,24,36,48";
		        		hou = "01,13,25,37,49";
		        		yang = "02,14,26,38";
		        		ma = "03,15,27,39";
		        		she = "04,16,28,40";
		        		lon = "05,17,29,41";
		        		tu = "06,18,30,42";
		        		hu = "07,19,31,43";
		        		niu = "08,20,32,44";
		            }else if(((year-4)%12)==9){ //2017年为鸡年
		        		shu = "10,22,34,46";
		        		zhu = "11,23,35,47";
		        		gou = "12,24,36,48";
		        		ji = "01,13,25,37,49";
		        		hou = "02,14,26,38";
		        		yang = "03,15,27,39";
		        		ma = "04,16,28,40";
		        		she = "05,17,29,41";
		        		lon = "06,18,30,42";
		        		tu = "07,19,31,43";
		        		hu = "08,20,32,44";
		        		niu = "09,21,33,45";
		            }else if(((year-4)%12)==10){
		        		shu = "11,23,35,47";
		        		zhu = "12,24,36,48";
		        		gou = "01,13,25,37,49";
		        		ji = "02,14,26,38";
		        		hou = "03,15,27,39";
		        		yang = "04,16,28,40";
		        		ma = "05,17,29,41";
		        		she = "06,18,30,42";
		        		lon = "07,19,31,43";
		        		tu = "08,20,32,44";
		        		hu = "09,21,33,45";
		        		niu = "10,22,34,46";
		            }else if(((year-4)%12)==11){
		        		shu = "12,24,36,48";
		        		zhu = "01,13,25,37,49";
		        		gou = "02,14,26,38";
		        		ji = "03,15,27,39";
		        		hou = "04,16,28,40";
		        		yang = "05,17,29,41";
		        		ma = "06,18,30,42";
		        		she = "07,19,31,43";
		        		lon = "08,20,32,44";
		        		tu = "09,21,33,45";
		        		hu = "10,22,34,46";
		        		niu = "11,23,35,47";
		            }
				}
				map.put("shu", shu);
				map.put("zhu", zhu);
				map.put("gou", gou);
				map.put("ji", ji);
				map.put("hou", hou);
				map.put("yang", yang);
				map.put("ma", ma);
				map.put("she", she);
				map.put("lon", lon);
				map.put("tu", tu);
				map.put("hu", hu);
				map.put("niu", niu);
				element = new Element(luyear, map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return (HashMap<String, String>) map;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getSflhcMap(String luyear,Date startTime) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("sflhcMap");
			Element element = cache.get(luyear);
			if (element != null) {
				map = (Map<String, String>) element.getObjectValue();
			} else {
				String ji = "01,13,25,37,49";// 鸡 2017
				String shu = "10,22,34,46";// 鼠 2017
				String hu = "08,20,32,44";// 虎 2017
				String lon = "06,18,30,42";// 龙 2017
				String ma = "04,16,28,40";// 马 2017
				String hou = "02,14,26,38";// 猴 2017
				String gou = "12,24,36,48";// 狗 2017
				String niu = "09,21,33,45";// 牛 2017
				String tu = "07,19,31,43";// 兔 2017
				String she = "05,17,29,41";// 蛇 2017
				String yang = "03,15,27,39";// 羊 2017
				String zhu = "11,23,35,47";// 猪 2017
				
				int year2 = DateTimeUtil.getRightYear(startTime);
				int month = DateTimeUtil.getRightMonth(startTime)-1;
				int day = DateTimeUtil.getRightDay(startTime);
				Calendar cal=Calendar.getInstance();
				cal.set(year2, month, day);
				Lunar lunar=new Lunar(cal);
				int year = lunar.year;
				if(ParamUtils.chkInteger(year)){
					if(((year-4)%12)==0){
						shu = "01,13,25,37,49";
						zhu = "02,14,26,38";
						gou = "03,15,27,39";
						ji = "04,16,28,40";
						hou = "05,17,29,41";
						yang = "06,18,30,42";
						ma = "07,19,31,43";
						she = "08,20,32,44";
						lon = "09,21,33,45";
						tu = "10,22,34,46";
						hu = "11,23,35,47";
						niu = "12,24,36,48";            
					}else if(((year-4)%12)==1){
						shu = "02,14,26,38";
						zhu = "03,15,27,39";
						gou = "04,16,28,40";
						ji = "05,17,29,41";
						hou = "06,18,30,42";
						yang = "07,19,31,43";
						ma = "08,20,32,44";
						she = "09,21,33,45";
						lon = "10,22,34,46";
						tu = "11,23,35,47";
						hu = "12,24,36,48";
						niu = "01,13,25,37,49";
					}else if(((year-4)%12)==2){
						shu = "03,15,27,39";
						zhu = "04,16,28,40";
						gou = "05,17,29,41";
						ji = "06,18,30,42";
						hou = "07,19,31,43";
						yang = "08,20,32,44";
						ma = "09,21,33,45";
						she = "10,22,34,46";
						lon = "11,23,35,47";
						tu = "12,24,36,48";
						hu = "01,13,25,37,49";
						niu = "02,14,26,38";
					}else if(((year-4)%12)==3){
						shu = "04,16,28,40";
						zhu = "05,17,29,41";
						gou = "06,18,30,42";
						ji = "07,19,31,43";
						hou = "08,20,32,44";
						yang = "09,21,33,45";
						ma = "10,22,34,46";
						she = "11,23,35,47";
						lon = "12,24,36,48";
						tu = "01,13,25,37,49";
						hu = "02,14,26,38";
						niu = "03,15,27,39";
					}else if(((year-4)%12)==4){
						shu = "05,17,29,41";
						zhu = "06,18,30,42";
						gou = "07,19,31,43";
						ji = "08,20,32,44";
						hou = "09,21,33,45";
						yang = "10,22,34,46";
						ma = "11,23,35,47";
						she = "12,24,36,48";
						lon = "01,13,25,37,49";
						tu = "02,14,26,38";
						hu = "03,15,27,39";
						niu = "04,16,28,40";
					}else if(((year-4)%12)==5){
						shu = "06,18,30,42";
						zhu = "07,19,31,43";
						gou = "08,20,32,44";
						ji = "09,21,33,45";
						hou = "10,22,34,46";
						yang = "11,23,35,47";
						ma = "12,24,36,48";
						she = "01,13,25,37,49";
						lon = "02,14,26,38";
						tu = "03,15,27,39";
						hu = "04,16,28,40";
						niu = "05,17,29,41";
					}else if(((year-4)%12)==6){
						shu = "07,19,31,43";
						zhu = "08,20,32,44";
						gou = "09,21,33,45";
						ji = "10,22,34,46";
						hou = "11,23,35,47";
						yang = "12,24,36,48";
						ma = "01,13,25,37,49";
						she = "02,14,26,38";
						lon = "03,15,27,39";
						tu = "04,16,28,40";
						hu = "05,17,29,41";
						niu = "06,18,30,42";
					}else if(((year-4)%12)==7){
						shu = "08,20,32,44";
						zhu = "09,21,33,45";
						gou = "10,22,34,46";
						ji = "11,23,35,47";
						hou = "12,24,36,48";
						yang = "01,13,25,37,49";
						ma = "02,14,26,38";
						she = "03,15,27,39";
						lon = "04,16,28,40";
						tu = "05,17,29,41";
						hu = "06,18,30,42";
						niu = "07,19,31,43";
					}else if(((year-4)%12)==8){
						shu = "09,21,33,45";
						zhu = "10,22,34,46";
						gou = "11,23,35,47";
						ji = "12,24,36,48";
						hou = "01,13,25,37,49";
						yang = "02,14,26,38";
						ma = "03,15,27,39";
						she = "04,16,28,40";
						lon = "05,17,29,41";
						tu = "06,18,30,42";
						hu = "07,19,31,43";
						niu = "08,20,32,44";
					}else if(((year-4)%12)==9){ //2017年为鸡年
						shu = "10,22,34,46";
						zhu = "11,23,35,47";
						gou = "12,24,36,48";
						ji = "01,13,25,37,49";
						hou = "02,14,26,38";
						yang = "03,15,27,39";
						ma = "04,16,28,40";
						she = "05,17,29,41";
						lon = "06,18,30,42";
						tu = "07,19,31,43";
						hu = "08,20,32,44";
						niu = "09,21,33,45";
					}else if(((year-4)%12)==10){
						shu = "11,23,35,47";
						zhu = "12,24,36,48";
						gou = "01,13,25,37,49";
						ji = "02,14,26,38";
						hou = "03,15,27,39";
						yang = "04,16,28,40";
						ma = "05,17,29,41";
						she = "06,18,30,42";
						lon = "07,19,31,43";
						tu = "08,20,32,44";
						hu = "09,21,33,45";
						niu = "10,22,34,46";
					}else if(((year-4)%12)==11){
						shu = "12,24,36,48";
						zhu = "01,13,25,37,49";
						gou = "02,14,26,38";
						ji = "03,15,27,39";
						hou = "04,16,28,40";
						yang = "05,17,29,41";
						ma = "06,18,30,42";
						she = "07,19,31,43";
						lon = "08,20,32,44";
						tu = "09,21,33,45";
						hu = "10,22,34,46";
						niu = "11,23,35,47";
					}
				}
				map.put("shu", shu);
				map.put("zhu", zhu);
				map.put("gou", gou);
				map.put("ji", ji);
				map.put("hou", hou);
				map.put("yang", yang);
				map.put("ma", ma);
				map.put("she", she);
				map.put("lon", lon);
				map.put("tu", tu);
				map.put("hu", hu);
				map.put("niu", niu);
				element = new Element(luyear, map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return (HashMap<String, String>) map;
	}
	
	/**
	 * 获得所有有效彩种
	 * 
	 * @return
	 */
	public static List<GaSessionInfo> getGameList() {
		List<GaSessionInfo> list = new ArrayList<GaSessionInfo>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameList");
			Element element = cache.get("gameList");
			if (element != null) {
				list = (List<GaSessionInfo>) element.getObjectValue();
			} else {
				list = gaService.findGaSessionInfoList();
				element = new Element("gameList", list);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获得所有显示开奖的彩种（wap开奖界面用）
	 * 
	 * @return
	 */
	public static List<GaSessionInfo> getShowOpenGameList() {
		List<GaSessionInfo> list = new ArrayList<GaSessionInfo>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameOpenList");
			Element element = cache.get("gameOpenList");
			if (element != null) {
				list = (List<GaSessionInfo>) element.getObjectValue();
			} else {
				HQUtils hq = new HQUtils(
					"from GaSessionInfo ga where ga.status='1' and ga.isShowHistoryOpen='1' order by ga.sort");
				List<Object> obList = baseDataService.findObjects(hq);
				element = new Element("gameOpenList", obList);
				cache.put(element);
				for (Object obj : obList) {
					list.add((GaSessionInfo)obj);
				}
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 更新显示开奖的彩种（wap开奖界面用）
	 * 
	 * @return
	 */
	public static void updateShowOpenGameList() {
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameOpenList");
			Element element = cache.get("gameOpenList");

			HQUtils hq = new HQUtils(
					"from GaSessionInfo ga where ga.status='1' and ga.isShowHistoryOpen='1' order by ga.sort");
				List<Object> obList = baseDataService.findObjects(hq);
			element = new Element("gameOpenList", obList);
			cache.put(element);
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新所有有效彩种  改变了GaSessionInfo的开奖期号 等值
	 * 
	 * @return
	 */
	public static void updateGameSessionInfo(GaSessionInfo sessionInfo) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("gameMap");
		Element element = cache.get("gameMap");
		if (element != null) {
			map = (LinkedHashMap<String, Object>) element.getObjectValue();
			if(map.get(sessionInfo.getGameType())!=null){
				map.remove(sessionInfo.getGameType());
			}
			map.put(sessionInfo.getGameType(), sessionInfo);
		} else {
			List<GaSessionInfo> list = gaService.findGaSessionInfoList();
			for (GaSessionInfo gaSessionInfo : list) {
				map.put(gaSessionInfo.getGameType(), gaSessionInfo);
			}
			if(map.get(sessionInfo.getGameType())!=null){
				map.remove(sessionInfo.getGameType());
			}
		}
		element = new Element("gameMap", map);
		cache.put(element);
	}

	/**
	 * 更新所有有效彩种  改变了GaSessionInfo的开奖期号 等值
	 * 
	 * @return
	 */
	public static void updateGameList(GaSessionInfo sessionInfo) {
//		List<GaSessionInfo> list = new ArrayList<GaSessionInfo>();
//		try {
//			CacheManager manager = CacheManager.create();
//			Cache cache = manager.getCache("gameList");
//			Element element = cache.get("gameList");
//
//			list = gaService.findGaSessionInfoList();
//			element = new Element("gameList", list);
//			cache.put(element);
//
//			// 2---
//			Cache cache2 = manager.getCache("gameMap");
//			Element element2 = cache2.get("gameMap");
//
//			Map<String, Object> map = new HashMap<String, Object>();
//			for (GaSessionInfo gaSessionInfo : list) {
//				map.put(gaSessionInfo.getGameType(), gaSessionInfo);
//			}
//			element2 = new Element("gameMap", map);
//			cache2.put(element2);
//		} catch (CacheException e) {
//			e.printStackTrace();
//		}
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("gameMap");
		Element element = cache.get("gameMap");
		if (element != null) {
			map = (LinkedHashMap<String, Object>) element.getObjectValue();
			map.put(sessionInfo.getGameType(), sessionInfo);
		} else {
			List<GaSessionInfo> list = gaService.findGaSessionInfoList();
			for (GaSessionInfo gaSessionInfo : list) {
				map.put(gaSessionInfo.getGameType(), gaSessionInfo);
			}
			map.put(sessionInfo.getGameType(), sessionInfo);
		}
		element = new Element("gameMap", map);
		cache.put(element);
	}
	
	/**
	 * 更新所有有效彩种
	 * 
	 * @return
	 */
	public static void updateGameList() {
		List<GaSessionInfo> list = new ArrayList<GaSessionInfo>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameList");
			Element element = cache.get("gameList");

			list = gaService.findGaSessionInfoList();
			element = new Element("gameList", list);
			cache.put(element);

			// 2---
			Cache cache2 = manager.getCache("gameMap");
			Element element2 = cache2.get("gameMap");

			Map<String, Object> map = new HashMap<String, Object>();
			for (GaSessionInfo gaSessionInfo : list) {
				map.put(gaSessionInfo.getGameType(), gaSessionInfo);
			}
			element2 = new Element("gameMap", map);
			cache2.put(element2);
			
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getGameMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameMap");
			Element element = cache.get("gameMap");
			if (element != null) {
				map = (HashMap<String, Object>) element.getObjectValue();
			} else {
				List<GaSessionInfo> list = gaService.findGaSessionInfoList();
				for (GaSessionInfo gaSessionInfo : list) {
					map.put(gaSessionInfo.getGameType(), gaSessionInfo);
				}
				element = new Element("gameMap", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return (HashMap<String, Object>) map;
	}

	
	/**
	 * 获取红包转盘设置
	 * @return
	 */
	public static LotterySetting getLotterySetting(String type) {
//		List<LotterySetting> list = new ArrayList<LotterySetting>();
		LotterySetting ls=new LotterySetting();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("lotterySetList");
			Element element = cache.get(type);
			if (element != null) {
				ls = (LotterySetting) element.getObjectValue();
			} else {
				if(type.equals("turntable")){//转盘
					ls= baseDataService
							.getLotterySetting(Constants.LOTTERY_SETTING_TURNTABLE);
				}else  if(type.equals("redpackets")){//红包
					ls = baseDataService
							.getLotterySetting(Constants.LOTTERY_SETTING_REDPACKETS);
				}
				element = new Element(type, ls);
				cache.put(element);
			}	
			//List<
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return ls;
	}
	
	/**
	 * 获取红包转盘设置
	 * @return
	 */
	public static List<LotterySettingRl> getLotterySettingRl(String type) {
		List<LotterySettingRl> list = new ArrayList<LotterySettingRl>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("lotterySetRlList");
			Element element = cache.get(type);
			if (element != null) {
				list = (List<LotterySettingRl>) element.getObjectValue();
			} else {
				if(type.equals("turntable")){//转盘
					LotterySetting lotterySetting = baseDataService
							.getLotterySetting(Constants.LOTTERY_SETTING_TURNTABLE);
					list = baseDataService.findLotterySetList(lotterySetting.getLsId());
				}else  if(type.equals("redpackets")){//红包
					LotterySetting lotterySetting = baseDataService
							.getLotterySetting(Constants.LOTTERY_SETTING_REDPACKETS);
					list = baseDataService.findLotterySetList(lotterySetting.getLsId());
				}
				element = new Element(type, list);
				cache.put(element);
			}	
			//List<
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 充值返水配置
	 * @param key member=会员 agent=代理
	 */
	public static ValueConfig rechargeBackConfig(String key){
		return rechargeBackConfig(key,false);
	}
	public static void rechargeBackConfigUpdate(){
		rechargeBackConfig("member",true);
		rechargeBackConfig("agent",true);
	}
	public static ValueConfig rechargeBackConfig(String key,boolean update){
		CacheManager manager;
		ValueConfig config = null;
		try {
			manager = CacheManager.create();
			Cache cache = manager.getCache("rechargeBackConfig");
			Element element = cache.get(key);
			if (element != null && !update) {
				config = (ValueConfig)element.getObjectValue();
			}else{
				//从数据库再次缓存
				HQUtils hq = null;
				if(key.equals("member")){//会员
					hq = new HQUtils("from Param p where p.status=? and (p.type=? or p.type=? or p.type=? or p.type=? or p.type=?)");
					hq.addPars(Constants.PUB_STATUS_OPEN);
					hq.addPars(Constants.PARAM_RECHARGE_BACK_56);
					hq.addPars(Constants.PARAM_RECHARGE_BACK_57);
					hq.addPars(Constants.PARAM_RECHARGE_BACK_58);
					hq.addPars(Constants.PARAM_RECHARGE_BACK_59);
					hq.addPars(Constants.PARAM_RECHARGE_BACK_60);
					hq.setOrderby(" order by p.paramId");
					List<Object> list = paramService.findObjects(hq);
					config = new ValueConfig();
					config.setOpen(list!=null && !list.isEmpty()?true:false);//无记录返水关闭
					Integer max=0,min=0;//最小，最大值
					BigDecimal maxPer=new BigDecimal(0),minPer=new BigDecimal(0);//最小最大比例
					List<ValueItem> items = new ArrayList<ValueItem>();//数值
					String val,remark;
					int count = 0;
					for(Object obj:list){
						Param p = (Param)obj;
						val = p.getValue();//0.1
						remark = p.getBeizhu();
						if(ParamUtils.chkString(val)){
							val = val.trim();
							ValueItem vi = new ValueItem();
							String[] mmArr = remark.split(",");
							Integer _min = Integer.valueOf(mmArr[0]);
							Integer _max = Integer.valueOf(mmArr[1]);
							BigDecimal _per = new BigDecimal(val);
							if(count==0){
								min = _min;
								max = _max;
								minPer = _per;
								maxPer = _per;
							}else{
								if(min>_min) min = _min;
								if(max<_max) max = _max;
								if(minPer.compareTo(_per)==1) minPer = _per;
								if(maxPer.compareTo(_per)==-1) maxPer = _per;
							}
							vi.setMin(_min);
							vi.setMax(_max);
							vi.setPercent(_per);//比例
							items.add(vi);
							count++;
						}
					}
					config.setMin(min);
					config.setMinPer(minPer);
					config.setMax(max);
					config.setMaxPer(maxPer);
					config.setItems(items);
					element = new Element(key, config);
					cache.put(element);
					
				}else if(key.equals("agent")){//代理
					hq = new HQUtils("from Param p where p.status=? and (p.type=? or p.type=? or p.type=? or p.type=? or p.type=?)");
					hq.addPars(Constants.PUB_STATUS_OPEN);
					hq.addPars(Constants.PARAM_RECHARGE_AGENT_BACK_61);
					hq.addPars(Constants.PARAM_RECHARGE_AGENT_BACK_62);
					hq.addPars(Constants.PARAM_RECHARGE_AGENT_BACK_63);
					hq.addPars(Constants.PARAM_RECHARGE_AGENT_BACK_64);
					hq.addPars(Constants.PARAM_RECHARGE_AGENT_BACK_65);
					hq.setOrderby(" order by p.paramId");
					List<Object> list = paramService.findObjects(hq);
					config = new ValueConfig();
					config.setOpen(list!=null && !list.isEmpty()?true:false);//无记录返水关闭
					Integer max=0,min=0;//最小，最大值
					BigDecimal maxPer=new BigDecimal(0),minPer=new BigDecimal(0);//最小最大比例
					List<ValueItem> items = new ArrayList<ValueItem>();//数值
					String val,remark;
					int count = 0;
					for(Object obj:list){
						Param p = (Param)obj;
						val = p.getValue();//0.1
						remark = p.getBeizhu();
						if(ParamUtils.chkString(val)){
							val = val.trim();
							ValueItem vi = new ValueItem();
							String[] mmArr = remark.split(",");
							Integer _min = Integer.valueOf(mmArr[0]);
							Integer _max = Integer.valueOf(mmArr[1]);
							BigDecimal _per = new BigDecimal(val);
							if(count==0){
								min = _min;
								max = _max;
								minPer = _per;
								maxPer = _per;
							}else{
								if(min>_min) min = _min;
								if(max<_max) max = _max;
								if(minPer.compareTo(_per)==1) minPer = _per;
								if(maxPer.compareTo(_per)==-1) maxPer = _per;
							}
							vi.setMin(_min);
							vi.setMax(_max);
							vi.setPercent(_per);//比例
							items.add(vi);
							count++;
						}
					}
					config.setMin(min);
					config.setMinPer(minPer);
					config.setMax(max);
					config.setMaxPer(maxPer);
					config.setItems(items);
					element = new Element(key, config);
					cache.put(element);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	/**
	 * 打码返水配置
	 * @param key member=会员 agent=代理
	 */
	public static ValueConfig betBackConfig(String key){
		return betBackConfig(key,false);
	}
	public static void betBackConfigUpdate(){
		betBackConfig("member",true);
		betBackConfig("agent",true);
	}
	public static ValueConfig betBackConfig(String key,boolean update){
		CacheManager manager;
		ValueConfig config = null;
		try {
			manager = CacheManager.create();
			Cache cache = manager.getCache("betBackConfig");
			Element element = cache.get(key);
			if (element != null && !update) {
				config = (ValueConfig)element.getObjectValue();
			}else{
				//从数据库再次缓存
				HQUtils hq = null;
				if(key.equals("member")){//会员
					hq = new HQUtils("from Param p where p.status=? and (p.type=? or p.type=? or p.type=? or p.type=? or p.type=?)");
					hq.addPars(Constants.PUB_STATUS_OPEN);
					hq.addPars(Constants.PARAM_BET_BACK_51);
					hq.addPars(Constants.PARAM_BET_BACK_52);
					hq.addPars(Constants.PARAM_BET_BACK_53);
					hq.addPars(Constants.PARAM_BET_BACK_54);
					hq.addPars(Constants.PARAM_BET_BACK_55);
					hq.setOrderby(" order by p.paramId");
					List<Object> list = paramService.findObjects(hq);
					config = new ValueConfig();
					config.setOpen(list!=null && !list.isEmpty()?true:false);//无记录返水关闭
					Integer max=0,min=0;//最小，最大值
					BigDecimal maxPer=new BigDecimal(0),minPer=new BigDecimal(0);//最小最大比例
					String execTime=null;//执行时间
					List<ValueItem> items = new ArrayList<ValueItem>();//数值
					String val,remark;
					int count = 0;
					for(Object obj:list){
						Param p = (Param)obj;
						val = p.getValue();//15:00:00&0.1
						remark = p.getBeizhu();
						if(ParamUtils.chkString(val) && val.contains("&")){
							ValueItem vi = new ValueItem();
							String[] valArr = val.split("&");
							String[] mmArr = remark.split(",");
							Integer _min = Integer.valueOf(mmArr[0]);
							Integer _max = Integer.valueOf(mmArr[1]);
							BigDecimal _per = new BigDecimal(valArr[1].trim());
							if(count==0){
								min = _min;
								max = _max;
								minPer = _per;
								maxPer = _per;
							}else{
								if(min>_min) min = _min;
								if(max<_max) max = _max;
								if(minPer.compareTo(_per)==1) minPer = _per;
								if(maxPer.compareTo(_per)==-1) maxPer = _per;
							}
							if(execTime==null){//调协执行时间
								execTime = valArr[0].trim().substring(0,5);
							}
							vi.setMin(_min);
							vi.setMax(_max);
							vi.setPercent(_per);//比例
							items.add(vi);
							count++;
						}
					}
					config.setExecTime(execTime);
					config.setMin(min);
					config.setMinPer(minPer);
					config.setMax(max);
					config.setMaxPer(maxPer);
					config.setItems(items);
					element = new Element(key, config);
					cache.put(element);
					
				}else if(key.equals("agent")){//代理
					hq = new HQUtils("from Param p where p.status=? and (p.type=? or p.type=? or p.type=? or p.type=? or p.type=?)");
					hq.addPars(Constants.PUB_STATUS_OPEN);
					hq.addPars(Constants.PARAM_BET_BACK_AGENT_66);
					hq.addPars(Constants.PARAM_BET_BACK_AGENT_67);
					hq.addPars(Constants.PARAM_BET_BACK_AGENT_68);
					hq.addPars(Constants.PARAM_BET_BACK_AGENT_69);
					hq.addPars(Constants.PARAM_BET_BACK_AGENT_70);
					hq.setOrderby(" order by p.paramId");
					List<Object> list = paramService.findObjects(hq);
					config = new ValueConfig();
					config.setOpen(list!=null && !list.isEmpty()?true:false);//无记录返水关闭
					Integer max=0,min=0;//最小，最大值
					BigDecimal maxPer=new BigDecimal(0),minPer=new BigDecimal(0);//最小最大比例
					String execTime=null;//执行时间
					List<ValueItem> items = new ArrayList<ValueItem>();//数值
					String val,remark;
					int count = 0;
					for(Object obj:list){
						Param p = (Param)obj;
						val = p.getValue();//15:00:00&0.1
						remark = p.getBeizhu();
						if(ParamUtils.chkString(val) && val.contains("&")){
							ValueItem vi = new ValueItem();
							String[] valArr = val.split("&");
							String[] mmArr = remark.split(",");
							Integer _min = Integer.valueOf(mmArr[0]);
							Integer _max = Integer.valueOf(mmArr[1]);
							BigDecimal _per = new BigDecimal(valArr[1].trim());
							if(count==0){
								min = _min;
								max = _max;
								minPer = _per;
								maxPer = _per;
							}else{
								if(min>_min) min = _min;
								if(max<_max) max = _max;
								if(minPer.compareTo(_per)==1) minPer = _per;
								if(maxPer.compareTo(_per)==-1) maxPer = _per;
							}
							if(execTime==null){//调协执行时间
								execTime = valArr[0].trim().substring(0,5);
							}
							vi.setMin(_min);
							vi.setMax(_max);
							vi.setPercent(_per);//比例
							items.add(vi);
							count++;
						}
					}
					config.setExecTime(execTime);
					config.setMin(min);
					config.setMinPer(minPer);
					config.setMax(max);
					config.setMaxPer(maxPer);
					config.setItems(items);
					element = new Element(key, config);
					cache.put(element);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	/**
	 * 获得返水比例时间[充值/打码返水通用]
	 * @param gameName 
	 * @return
	 */
	public static String getBetBack(String type) {
		CacheManager manager;
		String betting = null;
		try {
			manager = CacheManager.create();
			Cache cache = manager.getCache("betBack");
			Element element = cache.get(type);
			if (element != null) {
				betting = (String) element.getObjectValue();
			} else {
					if(type.equals("agentBetBack")){//代理返水
						Param param=paramService.getParamByType(Constants.PARAM_BET_SEND_AGENT);
						betting=param.getValue();
					}else if(type.equals("memberBetBack")){//个人返水
						LotterySetting lotterySetting = baseDataService
								.getLotterySetting(Constants.LOTTERY_SETTING_RECHARGE_BET_BACK);
						List<LotterySettingRl> list = baseDataService.findLotterySetList(lotterySetting.getLsId());
						String minMonry="";
						String maxMonry="";
						String rate="";
						if(list!=null&&list.size()>0){
							LotterySettingRl rl=list.get(0);
							minMonry=rl.getRechargeMinMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							maxMonry=rl.getRechargeMaxMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
							rate=rl.getFixedMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						}					
						betting=DateTimeUtil.DateToStringHHmmss(lotterySetting.getStartTime())+"&"+minMonry+"&"+maxMonry+"&"+rate;
					} else if (type.equals("memberBetBackStatus")) {//个人返水开关
						LotterySetting lotterySetting = baseDataService.getLotterySetting(Constants.LOTTERY_SETTING_RECHARGE_BET_BACK);
						betting = lotterySetting.getStatus();
					}
					if (ParamUtils.chkString(type)) {
						element = new Element(type, betting);
						cache.put(element);
					}
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return betting;
	}
	
	public static void updateTurnTableList() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("lotterySetList");
//		List<LotterySetting>list=new ArrayList<LotterySetting>();
		Element element = cache.get("turntable");
		LotterySetting lotterySetting = baseDataService
				.getLotterySetting(Constants.LOTTERY_SETTING_TURNTABLE);
//		list.add(lotterySetting);
		element = new Element("turntable", lotterySetting);
		cache.put(element);
		
		CacheManager manager1 = CacheManager.create();
		Cache cache1 = manager1.getCache("lotterySetRlList");
		List<LotterySettingRl>list1=new ArrayList<LotterySettingRl>();
		Element element1 = cache1.get("turntable");
		list1 = baseDataService.findLotterySetList(lotterySetting.getLsId());

		element1 = new Element("turntable", list1);
		cache1.put(element1);	

	}
	
	public static void updateRedPacketsList() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("lotterySetList");
//		List<LotterySetting>list=new ArrayList<LotterySetting>();
		Element element = cache.get("redpackets");
		LotterySetting lotterySetting = baseDataService
				.getLotterySetting(Constants.LOTTERY_SETTING_REDPACKETS);
//		list.add(lotterySetting);
		element = new Element("redpackets", lotterySetting);
		cache.put(element);
		
		CacheManager manager1 = CacheManager.create();
		Cache cache1 = manager1.getCache("lotterySetRlList");
		List<LotterySettingRl>list1=new ArrayList<LotterySettingRl>();
		Element element1 = cache1.get("redpackets");
		list1 = baseDataService.findLotterySetList(lotterySetting.getLsId());

		element1 = new Element("redpackets", list1);
		cache1.put(element1);
	}
	
	public static void updateAgentBetBack() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("betBack");
		Element element = cache.get("agentBetBack");
		Param param=paramService.getParamByType(Constants.PARAM_BET_SEND_AGENT);
		String betting=param.getValue();
		element = new Element("agentBetBack", betting);
		cache.put(element);
	}
	
	public static void updateMemberBetBack() {
		CacheManager manager = CacheManager.create();
		Cache cache = manager.getCache("betBack");
		Element element = cache.get("memberBetBack");
		LotterySetting lotterySetting = baseDataService
				.getLotterySetting(Constants.LOTTERY_SETTING_RECHARGE_BET_BACK);
		List<LotterySettingRl> list = baseDataService.findLotterySetList(lotterySetting.getLsId());
		String minMonry="";
		String maxMonry="";
		String rate="";
		if(list!=null&&list.size()>0){
			LotterySettingRl rl=list.get(0);
			minMonry=rl.getRechargeMinMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			maxMonry=rl.getRechargeMaxMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			rate=rl.getFixedMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}
		String betting=DateTimeUtil.DateToStringHHmmss(lotterySetting.getStartTime())+"&"+minMonry+"&"+maxMonry+"&"+rate;
		element = new Element("memberBetBack", betting);
		cache.put(element);
	}
	
	/**
	 * 根据彩种查询GaSessionInfo
	 * @param gameType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GaSessionInfo getGaSessionInfo(String gameType) {
		Map<String, GaSessionInfo> map = new HashMap<String, GaSessionInfo>();
		try {
			CacheManager manager = CacheManager.create();
			Cache cache = manager.getCache("gameMap");
			Element element = cache.get("gameMap");
			if (element != null) {
				map = (Map<String, GaSessionInfo>) element.getObjectValue();
			} else {
				List<GaSessionInfo> allList = gaService.findGaSessionInfoList();
				for (GaSessionInfo gaSessionInfo : allList) {
					map.put(gaSessionInfo.getGameType(), gaSessionInfo);
				}
				element = new Element("gameMap", map);
				cache.put(element);
			}
		} catch (CacheException e) {
			e.printStackTrace();
		}
		GaSessionInfo gaSessionInfo = map.get(gameType);
		
		return gaSessionInfo;
	}
}
