define(['jquery'],function($){

    //公共数据
    var common = {
        ssc : {
            'num'      : [0,1,2,3,4,5,6,7,8,9],
            'num_len'  : 10,
            'col_td'   : 8,
            'open_len' : 5,
            'zj_val'   : 4,
            'max'      : 9,
            'da'       : [5,6, 7, 8, 9],
            'xiao'     : [0, 1, 2, 3, 4],
            'dan'      : [1, 3, 5, 7, 9],
            'shuang'   : [0, 2, 4, 6, 8],
            'zhi'      : [1, 2, 3, 5, 7],
            'he'       : [0, 4, 6, 8, 9]
        },
        pk10 : {
            'num'      : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            'num_len'  : 10,
            'col_td'   : 9,
            'open_len' : 10,
            'zj_val'   : 5,
            'max'      : 10,
            'da'       : [6, 7, 8, 9, 10],
            'xiao'     : [1, 2, 3, 4, 5],
            'dan'      : [1, 3, 5, 7, 9],
            'shuang'   : [2, 4, 6, 8, 10]
        },
        klsf : {
            'num'      : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],
            'num_len'  : 20,
            'col_td'   : 9,
            'open_len' : 8,
            'zj_val'   : 10,
            'max'      : 20,
            'da'       : [11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
            'xiao'     : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            'dan'      : [1, 3, 5, 7, 9, 11, 13, 15, 17, 19],
            'shuang'   : [2, 4, 6, 8, 10, 12, 14, 16, 18, 20],
            'dong'     : [1, 5, 9, 13, 17],
            'nan'      : [2, 6, 10, 14, 18],
            'xi'       : [3, 7, 11, 15, 19],
            'bei'      : [4, 8, 12, 16, 20],
            'zhong'    : [1, 2, 3, 4, 5, 6, 7],
            'fa'       : [8, 9, 10, 11, 12, 13, 14],
            'bai'      : [15, 16, 17, 18, 19, 20]
        }
    };
    
    //返回全局方法
    return app = {
        //彩种的信息
        czinfo : {
            'pk10'    : $.extend({},common.pk10),
            'speed10' : $.extend({},common.pk10),
            'jspk10'  : $.extend({},common.pk10),
            'xyft'    : $.extend({},common.pk10),
            'cq_ssc'  : $.extend({},common.ssc),
            'speed5'  : $.extend({},common.ssc),
            'jsssc'   : $.extend({},common.ssc),
            'gd_klsf' : $.extend({},common.klsf),
            'xync'    : $.extend({},common.klsf),
            'tj_ssc'  : $.extend({},common.ssc),
            'xj_ssc'  : $.extend({},common.ssc),
            'js_k3' : {
                'num'      : [1,2,3,4,5,6],
                'num_len'  : 6,
                'col_td'   : 9,
                'open_len' : 3,
                'zj_val'   : 3,
                'max'      : 6
            }
        },
        reload : false,
        trend : {
            arr : [],
            category : []
        },
        sort : function(a,b){
            return a-b;
        },
        geturlstr:function(name){
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
            var r = window.location.search.substr(1).match(reg);
            if (r!=null) return decodeURIComponent((r[2])); return null;
        },
        tab : function(nav,box,way){
            $(document).on(way,nav,function(){
                $(this).addClass('active').siblings(nav).removeClass('active');
                $(box).eq($(this).index()).show().siblings(box).hide();
            });
            $(nav).eq(0).trigger(way);
        },
        add_class : function(obj,class_name){
            $(obj).addClass(class_name);
        },
        shoucang : function(){
            var title = document.title, url = window.location.href;
            $('.add_sc').click(function(){
                try {
                    window.external.addFavorite(url, title);
                }
                catch (e) {
                    try {
                        window.sidebar.addPanel(title, url, "");
                    }
                    catch (e) {
                        alert("加入收藏失败，请使用Ctrl+D进行添加");
                    }
                }
            });
        },
        //开奖历史走势图
        get_trend : function(type){
            var timestamp = new Date().getTime();
            //走势曲线图
            $.ajax({
                url : '/api/recent?code=' + type + '&t=' + timestamp,
                method : 'get',
                success : function(result){
                    if(result.code === 0){

                        for (var i = 0, len = result.data.length; i < len; i++) {
                            app.trend.arr.push({
                                issue : result.data[i].issue,
                                array : result.data[i].array,
                                time  : result.data[i].time,
                                y     : parseInt(result.data[i].array[0])
                            });
                            app.trend.category.push(result.data[i].term);
                        }

                        app.highcharts({
                            obj : '.trend-box',
                            type : type,
                            max : app.czinfo[type].max,
                            category : app.trend.category,
                            data : app.trend.arr
                        });

                    }else{
                        app.loadmark({text:'走势图加载失败，请刷新重试',time:1100});
                    }
                },
                error : function(){
                    app.loadmark({text:'走势图加载失败，请刷新重试',time:1100});
                }
            });
        },
        //走势图插件配置
        highcharts : function(option){

            require(['highcharts'],function(highcharts){
            
                $(option.obj).highcharts({
                    title: {text: null},
                    chart:{type:'spline'},
                    exporting:{enabled:false},
                    xAxis: {
                        labels:{rotation:0},
                        gridLineWidth:1,
                        categories: option.category
                    },
                    yAxis: {
                        minorTickInterval: 'auto',
                        title:{text:null},
                        min: 0,
                        max: option.max
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 1,
                            dataLabels: {
                                enabled: true,
                                style:{"color":"red", "fontSize":"12px"}
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true,
                        shared: true,
                        formatter:function(){
                            var k = this.points[0].point.index;
                            var d = this.points[0].series['data'][k];
                            var h = '';
                            for(var i = 0, len = d.array.length; i < len; i++){
                                h += '<span class="n'+d.array[i]+'">'+d.array[i]+'</span>';
                            }
                            return '<div style="text-align:center;margin-bottom:5px;">第'+d.issue+'期&nbsp;&nbsp;&nbsp;&nbsp;'+(d.time).substring(0,16)+'</div><div class="cai-num '+option.type+'-num size-32 center">'+h+'</div>';
                        },
                        crosshairs: [
                        {
                            width: 1,
                            color: 'red',
                            dashStyle: 'shortdot'
                        },
                        {
                            width: 1,
                            color: 'red',
                            dashStyle: 'shortdot'
                        }]
                    },
                    legend:{enabled:false},
                    series: [{
                        data: option.data
                    }]
                });

            });

        },
        //筛选还原
        reset_active : function (type){
            var obj = '';
            if(type && type == 'type'){
                obj = $('.btn-filter-type a');
            }else if(type && type == 'num'){
                obj = $('.btn-filter-num a');
            }else if(type && type == 'all'){
                obj = $('.btn-filter-num a,.btn-filter-type a');
            }
            obj != '' && obj.removeClass('active');
            $('.history-table .td-num span').removeClass('opacity');
        },
        //并列筛选， 返回共同的值
        get_common_nums : function (arr){

            //循环数组，对值进行计数，如果值的个数等于所选分类个数，那么这个值就是所有数组共同存在的

            var json = {}, len = arr.length, class_name = '';

            for(var i = 0 ; i < len ; i++){

                for(var j = 0, arr_len = arr[i].length; j < arr_len; j++){

                    if(!json[arr[i][j]]){

                        json[arr[i][j]] = 1;

                    }else{
                        json[arr[i][j]] ++ ;
                    }

                    if(json[arr[i][j]] == len){
                        class_name += '.n'+arr[i][j]+',';
                    }

                }

            }

            //去掉字符串最后一个逗号
            class_name = class_name.substring(0,class_name.length-1);

            $(class_name).removeClass('opacity');

        },
        //返回开奖号码的html结构
        nums_str : function(obj,arr,type){

            var num_str = '';

            if(type == 'dx'){
                for(var i = 0, len = arr.length; i < len ; i++){
                    num_str += '<span class="n'+arr[i]+' n-'+(parseInt(arr[i]) > app.czinfo[obj].zj_val ? 'da' : 'xiao')+'" data-num="'+arr[i]+'">'+arr[i]+'</span>';
                }
            }else if(type == 'ds'){
                for(var i = 0, len = arr.length; i < len ; i++){
                    num_str += '<span class="n'+arr[i]+' n-'+(parseInt(arr[i]) % 2 == 0 ? 'shuang' : 'dan')+'" data-num="'+arr[i]+'">'+arr[i]+'</span>';
                }
            }else{
                for(var i = 0, len = arr.length; i < len ; i++){
                    num_str += '<span class="n'+arr[i]+'" data-num="'+arr[i]+'">'+arr[i]+'</span>';
                }
            }

            return num_str;

        },
        //内页开奖信息tr结构
        page_lottery_tr : function(type,data){

            var arr = data.newest.array, td_nums_str = '', td_desc_str = '', mr_td_num_str = '<div style="display:block;" class="td-box cai-num size-32 center '+type+'-num">'+app.nums_str(type,arr)+'</div><div class="td-box cai-num size-32 center type-num">'+app.nums_str(type,arr,'dx')+'</div><div class="td-box cai-num size-32 center type-num">'+app.nums_str(type,arr,'ds')+'</div>';

            if(type == 'pk10' || type == 'xyft' || type == 'speed10'){

                td_desc_str = '<td width="55">'+(parseInt(arr[0])+parseInt(arr[1]))+'</td><td width="55">'+app.dx((parseInt(arr[0])+parseInt(arr[1])),11,'d')+'</td><td width="55">'+app.ds((parseInt(arr[0])+parseInt(arr[1])))+'</td><td width="55">'+app.lh(arr[0],arr[9])+'</td><td width="55">'+app.lh(arr[1],arr[8])+'</td><td width="55">'+app.lh(arr[2],arr[7])+'</td><td width="55">'+app.lh(arr[3],arr[6])+'</td><td width="55">'+app.lh(arr[4],arr[5])+'</td>';

            }else if(type == 'cq_ssc' || type == 'xj_ssc' || type == 'tj_ssc' || type == 'speed5'){

                td_nums_str = '<div class="td-box cai-num size-32 center type-num zh-num">'+app.nums_str(type,arr)+'</div>';
                //<td width="55">'+app.lh(arr[0],arr[4],'l')+'</td>
                td_desc_str = '<td width="55">'+data.newest.sum+'</td><td width="55">'+app.dx(data.newest.sum,22,'d')+'</td><td width="55">'+app.ds(data.newest.sum)+'</td><td width="55">'+app.shun(arr.slice(0,3))+'</td><td width="55">'+app.shun(arr.slice(1,4))+'</td><td width="55">'+app.shun(arr.slice(2,5))+'</td>';

            }else if(type == 'js_k3'){

                mr_td_num_str = '<div class="cai-num size-32 center '+type+'-num">'+app.nums_str(type,arr)+'</div>';

                td_desc_str = '<td width="90">'+data.newest.sum+'</td><td width="90">'+app.tc(data.newest.sum,arr)+'</td><td width="90">'+app.ds(data.newest.sum)+'</td>';

            }else if(type == 'gd_klsf' || type == 'xync'){

                td_nums_str = '<div class="td-box cai-num size-32 center type-num fw-num">'+app.nums_str(type,arr)+'</div><div class="td-box cai-num size-32 center type-num zfb-num">'+app.nums_str(type,arr)+'</div>';

                td_desc_str = '<td width="60">'+data.newest.sum+'</td><td width="60">'+app.dx(data.newest.sum,84,'d')+'</td><td width="60">'+app.ds(data.newest.sum,'s')+'</td><td width="60">'+(data.newest.sum % 10 > 4 ? '尾大' : '尾小')+'</td><td width="60">'+app.lh(arr[0],arr[7])+'</td><td width="60">'+app.lh(arr[1],arr[6])+'</td><td width="60">'+app.lh(arr[2],arr[5])+'</td><td width="60">'+app.lh(arr[3],arr[4])+'</td>';

            }

            return '<tr class="tr" id="tr-'+data.newest.issue+'"><td>'+data.newest.issue+'&nbsp;&nbsp;'+app.date_format(data.newest.time,'HH:mm:ss')+'</td><td class="td-num td-'+type+'">' + mr_td_num_str + td_nums_str +  '</td>' + td_desc_str+'</tr>';

        },
        //开奖中动画
        drawing : function(type){

            clearInterval(app.czinfo[type].open_timer);

            var open_len = app.czinfo[type].open_len, num_len = app.czinfo[type].num_len, nums = app.czinfo[type].num;

            app.czinfo[type].open_timer = setInterval(function(){

                var str = '';

                for(var i=0;i<open_len;i++){

                    var j = Math.floor(Math.random() * num_len);

                    str += '<span class="n'+nums[j]+'">'+nums[j]+'</span>';

                }

                $('#'+type).find('.cai-num').html(str);
                $('#'+type).find('.djs').html('开奖中');

            },110);

        },
        //获取开奖数据
        get_lottery : function(type){

            //开奖动画
            app.drawing(type);
            
            //时间戳
            var timestamp  = new Date().getTime();
            
            $.ajax({
                url : '/api/newest?code=' + type +'&t=' + timestamp,
                method : 'get',
                success : function(result){

                    if(result.code === 0){

                        var data = result.data, cha = data.current - data.newest.issue; app.czinfo[type].ticking = data.ticking;

                        //最新一期，或者最后一期，或者当天一期没开的情况，开始倒计时，准备开奖
                        if(cha > 2 || cha == 1 || cha == 0 || !data.newest.issue){

                            //倒计时开奖
                            app.countdown(type);
                            //清除开奖中动画定时器
                            clearInterval(app.czinfo[type].open_timer);
                            //开奖信息
                            app.lottery_result(type,data);

                        }else{

                            //如果不是最新一期，准备开奖
                            app.plan_draw(type,data);
                            //重试获取数据
                            app.get_retry(type);

                            //是否刷新页面
                            app.reload = true;

                        }
                        
                    }else{
                        app.get_retry(type);
                    }

                }
            });

        },
        //重试开奖
        get_retry : function(type){
            clearTimeout(app.czinfo[type].timeout);
            app.czinfo[type].timeout = setTimeout(function(){
                app.get_lottery(type);
                return;
            },1000);
        },
        //准备开奖
        plan_draw : function(type,data){
            $('#'+type).find('.current,.next').html(data.newest.issue+1);
            $('#'+type).find('.open').html(data.currentNo);
            $('#'+type).find('.left').html(data.remain);
        },
        //开奖倒计时
        countdown : function(type){

            //显示倒计时时间
            $('#'+type).find('.djs').html(app.format_time(app.czinfo[type].ticking,'开奖中'));

            clearInterval(app.czinfo[type].timer);

            //开始倒计时
            app.czinfo[type].timer = setInterval(function(){

                app.czinfo[type].ticking--;

                if(app.czinfo[type].ticking < 0){
                    clearInterval(app.czinfo[type].timer);
                    app.get_lottery(type);
                    return;
                }

                $('#'+type).find('.djs').html(app.format_time(app.czinfo[type].ticking,'开奖中'));

            },1000);

        },
        //渲染开奖数据
        lottery_result : function(type,data){

            $('#'+type).find('.current').html(data.newest.issue ? data.newest.issue : data.current);
            $('#'+type).find('.open').html(data.currentNo-1);
            $('#'+type).find('.left').html(data.remain+1);
            $('#'+type).find('.next').html(data.current);

            var num_str = data.newest.array ? app.nums_str(type,data.newest.array) : '等待开奖';

            $('#'+type).find('.cai-num').html(num_str);

            //蓝色模板，首页彩种表格信息
            if($('#'+type).find('.summery') && data.summery){
                $('#'+type).find('.summery td').each(function(i){
                    $(this).html(data.summery[i]);
                });
            }

            //如果有走势图，重新渲染走势图
            if($('.trend-box').length > 0 && data.newest.issue && $('#tr-'+data.newest.issue).length == 0){
                //删除第一条数据
                app.trend.arr.shift();
                app.trend.category.shift();
                //插入最新一条数据
                app.trend.arr.push({
                    issue : data.newest.issue,
                    array : data.newest.array,
                    time  : data.newest.time,
                    y     : parseInt(data.newest.array[0])
                });
                app.trend.category.push(data.newest.term);

                app.highcharts({
                    obj : '.trend-box',
                    type : type,
                    max : app.czinfo[type].max,
                    category : app.trend.category,
                    data : app.trend.arr
                });
            }

             //在内页开奖成功，并且时间是今天，插入最新一条数据
            if($('#table-'+type).length > 0 && data.newest.issue && $('#tr-'+data.newest.issue).length == 0 && app.is_today(type)){

                $('#table-'+type).find('tr:first').after(app.page_lottery_tr(type, data));

                $('#table-'+type).find('.tab-a a:first').click();

            }

            //app.reload = true;

            //内页开奖结束刷新数据
            if(app.reload && $('#dewdrop').length > 0){
                
                //只有今天，点击刷新数据
                if($('.day-a a.active').index() === 0){
                    $('.day-a a.active').trigger('click');
                }

            }else if(app.reload && $('#hot-table').length > 0){

                app.hot_reload($('#hot-table').data('type'));

            }else if(app.reload && $('#kill-table').length > 0){
                
                app.kill_reload($('#kill-table').data('type'));

            }else if(app.reload && $('#pursue-table').length > 0){

                app.pursue_reload($('#pursue-table').data('type'));

            }else if(app.reload && $('#trend-cont').length > 0){

                app.trend_reload($('#trend-cont').data('type'), data);

            }

            //第一次开奖结束，不刷新
            app.reload = true;

        },
        //是否是今天
        is_today : function(type){

            var today = app.get_date('yyyyMMdd'), url_date = window.location.href.split(type+'-')[1].split('.html')[0];

            return url_date == 'today' || url_date == today ? true : false;

        },
        //按日期筛选
        filter_date : function(type){

            var
            //今天日期
            today = app.get_date('yyyyMMdd'),
            //url路径参数
            pathname = window.location.pathname,
            //url中的时间字符串
            url_date = type == 'trend' ? pathname.split('/')[1].split('-')[3].split('.')[0] : url_date = pathname.split('/')[1].split('-')[2].split('.')[0],
            //页面日期
            page_day = window.location.href.indexOf('today') > 0 ? today : url_date,
            //页面日期格式化
            page_day_format = page_day.slice(0,4) + '-' + page_day.slice(4,6) + '-' + page_day.slice(6,8),
            //选择日期
            choose_day;

            //默认日期筛选显示当前页面日期
            if(type != 'dewdrop'){
                $('#date').val(page_day_format);
            }

            //选择日期
            $(document).on('click','.k-days span',function(){

                choose_day = $('#date').val().replace(/-/g,'');

                if(choose_day <= today){

                    window.location.href = pathname.replace(url_date, choose_day);

                }else{
                    app.loadmark({text:'日期选择不能超过今天',time:1100});
                    $('#date').val(page_day_format);
                }

                $('#date').blur();

            });

        },
        //获取倒计时时间
        get_countdown : function(type){

            //时间戳
            var timestamp  = new Date().getTime();
            
            $.ajax({
                url : '/api/newest?code=' + type +'&t=' + timestamp,
                method : 'get',
                success : function(result){
                    if(result.code === 0){
                        app.live_djs(type,result.data.ticking);
                    }
                }
            });

        },
        //开奖直播列表页倒计时
        live_djs : function(type,ticking){

            app.czinfo[type].live_ticking = ticking;

            $('#'+type).find('.second').html(ticking);

            clearInterval(app.czinfo[type].live_timer);

            //开始倒计时
            app.czinfo[type].live_timer = setInterval(function(){

                app.czinfo[type].live_ticking--;

                if(app.czinfo[type].live_ticking < 0){
                    clearInterval(app.czinfo[type].live_timer);
                    app.get_countdown(type);
                    return;
                }

                $('#'+type).find('.second').html(app.czinfo[type].live_ticking);

            },1000);

        },
        hot_reload : function(type){

            //时间戳
            var timestamp  = new Date().getTime();
            
            $.ajax({
                url : '/api/hot?code=' + type +'&t=' + timestamp,
                method : 'get',
                success : function(result){

                    if(result.code === 0){
                        
                        $('#hot-table tbody').html(app.hot_tbody(result.data, type));

                    }

                }
            });

        },
        hot_tbody : function(data, type){

            var tr = '';

            $.each(data, function(rank, val){

                var td = '';

                $.each(val, function(i, nums){

                    var number = '';

                    $.each(nums, function(num, times){
                        number += '<span class="n'+num+'">'+num+'</span>';
                    });

                    td += '<td><div class="cai-num '+type+'-num size-32">'+number+'</div></td>';

                });

                tr += '<tr class="tr"><td>'+rank+'</td>'+td+'</tr>';

            });

            return tr;

        },
        kill_reload : function(type){

            //时间戳
            var timestamp  = new Date().getTime();
            
            $.ajax({
                url : '/api/kill?code=' + type + '&ball=' + $('.page-tab a.active').index() +'&t=' + timestamp,
                method : 'get',
                success : function(result){

                    if(result.code === 0){
                        
                        $('#kill-table tbody').html(app.kill_tbody(result.data, type));

                        if(type == 'pk10' || type == 'speed10'){
                            $('.page-title .tab a.active').trigger('click');
                        }

                    }

                }
            });

        },
        kill_tbody : function(data, type){

            var tr = '', total = 0, a_true = 0, b_true = 0, c_true = 0, d_true = 0, e_true = 0, f_true = 0, g_true = 0;

            $.each(data, function(i, item){

                tr +=  '<tr class="tr">\
                        <td>'+item.issue+'期</td>\
                        <td class="num-td">'+(item.winning ? '<div class="cai-num '+type+'-num size-32 center">' + app.nums_str('kill', item.winning.split(',')) + '</div>' : '等待开奖')+'</td>\
                        <td>杀: '+item.planA+'</td>\
                        '+(item.resultA != null ? ('<td class="c-'+(item.resultA === 1 ? 'true' : 'false')+'">'+(item.resultA === 1 ? '√' : '×')+'</td>') : '<td>-</td>')+'\
                        <td class="bg-gray">杀: '+item.planB+'</td>\
                        '+(item.resultB != null ? ('<td class="bg-gray c-'+(item.resultB === 1 ? 'true' : 'false')+'">'+(item.resultB === 1 ? '√' : '×')+'</td>') : '<td class="bg-gray">-</td>')+'\
                        <td>杀: '+item.planC+'</td>\
                        '+(item.resultC != null ? ('<td class="c-'+(item.resultC === 1 ? 'true' : 'false')+'">'+(item.resultC === 1 ? '√' : '×')+'</td>') : '<td>-</td>')+'\
                        <td class="bg-gray">杀: '+item.planD+'</td>\
                        '+(item.resultD != null ? ('<td class="bg-gray c-'+(item.resultD === 1 ? 'true' : 'false')+'">'+(item.resultD === 1 ? '√' : '×')+'</td>') : '<td class="bg-gray">-</td>')+'\
                        <td>杀: '+item.planE+'</td>\
                        '+(item.resultE != null ? ('<td class="c-'+(item.resultE === 1 ? 'true' : 'false')+'">'+(item.resultE === 1 ? '√' : '×')+'</td>') : '<td>-</td>')+'\
                        <td class="td-f bg-gray">杀: '+item.planF+'</td>\
                        '+(item.resultF != null ? ('<td class="td-f bg-gray c-'+(item.resultF === 1 ? 'true' : 'false')+'">'+(item.resultF === 1 ? '√' : '×')+'</td>') : '<td class="td-f bg-gray">-</td>')+'\
                        <td class="td-g">杀: '+item.planG+'</td>\
                        '+(item.resultG != null ? ('<td class="td-g c-'+(item.resultG === 1 ? 'true' : 'false')+'">'+(item.resultG === 1 ? '√' : '×')+'</td>') : '<td class="td-g">-</td>')+'\
                        </tr>';

                total  = item.resultA != null ? total + 1 : total;
                a_true = item.resultA === 1 ? a_true + 1 : a_true;
                b_true = item.resultB === 1 ? b_true + 1 : b_true;
                c_true = item.resultC === 1 ? c_true + 1 : c_true;
                d_true = item.resultD === 1 ? d_true + 1 : d_true;
                e_true = item.resultE === 1 ? e_true + 1 : e_true;
                f_true = item.resultF === 1 ? f_true + 1 : f_true;
                g_true = item.resultG === 1 ? g_true + 1 : g_true;

            });

            tr = tr + '<tr class="tr">\
                            <td colspan="2">成绩统计</td>\
                            <td colspan="2" class="bg-gray">杀号算法A</td>\
                            <td colspan="2">杀号算法B</td>\
                            <td colspan="2" class="bg-gray">杀号算法C</td>\
                            <td colspan="2">杀号算法D</td>\
                            <td colspan="2" class="bg-gray">杀号算法E</td>\
                            <td colspan="2" class="td-f">杀号算法F</td>\
                            <td colspan="2" class="bg-gray td-g">杀号算法G</td>\
                        </tr>\
                        <tr class="tr">\
                            <td colspan="2">对错次数</td>\
                            <td colspan="2" class="bg-gray">'+total+'期对'+a_true+'期</td>\
                            <td colspan="2">'+total+'期对'+b_true+'期</td>\
                            <td colspan="2" class="bg-gray">'+total+'期对'+c_true+'期</td>\
                            <td colspan="2">'+total+'期对'+d_true+'期</td>\
                            <td colspan="2" class="bg-gray">'+total+'期对'+e_true+'期</td>\
                            <td colspan="2" class="td-f">'+total+'期对'+f_true+'期</td>\
                            <td colspan="2" class="bg-gray td-g">'+total+'期对'+g_true+'期</td>\
                        </tr>\
                        <tr class="tr">\
                            <td colspan="2">胜率统计</td>\
                            <td colspan="2" class="bg-gray">'+Math.round((a_true/total)*100)+'%</td>\
                            <td colspan="2">'+Math.round((b_true/total)*100)+'%</td>\
                            <td colspan="2" class="bg-gray">'+Math.round((c_true/total)*100)+'%</td>\
                            <td colspan="2">'+Math.round((d_true/total)*100)+'%</td>\
                            <td colspan="2" class="bg-gray">'+Math.round((e_true/total)*100)+'%</td>\
                            <td colspan="2" class="td-f">'+Math.round((f_true/total)*100)+'%</td>\
                            <td colspan="2" class="bg-gray td-g">'+Math.round((g_true/total)*100)+'%</td>\
                        </tr>\
                        ';

            return tr;

        },
        pursue_reload : function(type){

            //时间戳
            var timestamp  = new Date().getTime();
            
            $.ajax({
                url : '/api/pursue?code=' + type + '&ball=' + $('.page-tab a.active').index() +'&t=' + timestamp,
                method : 'get',
                success : function(result){

                    if(result.code === 0){
                        
                        $('#pursue-table tbody').html(app.pursue_tbody(result.data, type));

                    }

                }
            });

        },
        pursue_tbody : function(data, type){

            var tr = '';

            $.each(data, function(i, item){

                tr += '<tr class="tr">\
                        <td>'+item.issue+'期</td>\
                        <td>'+(item.winning ? '<div class="cai-num '+type+'-num size-32 center">'+app.nums_str('pursue', item.winning.split(','))+'</div>' : '等待开奖')+'</td>\
                        <td><div class="cai-num '+type+'-num size-32 center">'+app.nums_str('pursue', item.plan.split(','))+'</div></td>\
                        '+(item.result == null || item.result === 0 ? '<td>'+(item.result === 0 ? '追号失败' : '继续追号')+'</td>' : '<td class="'+(item.check === 1 ? 'bg-red' : 'bg-blue')+'">'+(item.check === 1 ? '当期中出' : item.check + '期中出')+'</td>')+'\
                    </tr>';

            });

            return tr;

        },
        trend_reload : function(type, data){

            var trend_type = 'basic',
                ball = $('#trend-cont .filter-down .fr a.active').index(),
                today = app.get_date('yyyyMMdd'),
                page_day = $('#date').val().replace(/-/g, ''),
                timestamp  = new Date().getTime();

            if($('#trend-cont .filter-down .tab-a a.active').index() > 0){
                trend_type = $('#trend-cont .filter-down .tab-a a.active').index() == 1 ? 'number' : 'sum';
            }

            //基本走势图
            if($('#trend-cont .filter-top>a.active').index() == 0){

                if(page_day == today){
                    $.ajax({
                        url : '/api/'+trend_type+'-trend',
                        data : {
                            code : type,
                            ball : ball,
                            t    : timestamp
                        },
                        method : 'get',
                        success : function(result){

                            if(result.code === 0){
                                
                                app.trend_tbody(result.data, type, trend_type, data);

                            }

                        }
                    });
                }

            }else{

                //同期走势图
                if(data.newest.issue && $('.filter-time select option[value="'+data.newest.issue+'"]').length == 0){
                    $('.filter-time select').prepend('<option value="'+data.newest.issue+'">'+data.newest.issue+'</option>');
                }

            }

        },
        trend_tbody : function(data, type, trend_type, open_data){

            //统计表格
            var sum_td = '', max_td = '', tr = '', open_issue = '';
            $.each(data.sum, function(i, item){
                sum_td += '<td>'+item+'</td>';
            });
            $.each(data.max, function(i, item){
                max_td += '<td>'+item+'</td>';
            });
            $('#tj-table tr.tr:first').html('<td>总次数</td>'+sum_td);
            $('#tj-table tr.tr:last').html('<td>最大遗漏</td>'+max_td);

            //走势表格
            $.each(data.trend, function(issue, val){
                
                var tds = '';

                $.each(val, function(i, val){
                    tds += '<td class="'+(val === 0 ? 'td-active' : '')+'">'+val+'</td>';
                });

                tr += '<tr class="tr-'+issue+'"><td>'+issue+'</td>'+tds+'</tr>';

                open_issue = issue;

            });
            
            //插入最新数据
            if($('.tr-'+open_issue).length == 0){
                $('#trend-table tbody').prepend(tr);
                //走势图数据处理
                app.trend_td(trend_type);
            }

        },
        trend_td : function(trend_type){
            $('#trend-table td.td-active').each(function(){
                
                var td_name = $('#trend-table thead tr:last th').eq($(this).index()-1).html();
                
                if(td_name == '反向' || td_name == '重号' || td_name == '正向'){
                    
                    $(this).html(td_name.split('')[0]).addClass('f-blue');

                }else if(td_name == '单' || td_name == '双' || td_name == '前' || td_name == '后'){
                    
                    $(this).html(td_name).addClass('f-orange');

                }else if(td_name == '大' || td_name == '小'){

                    $(this).html(td_name).addClass('f-green');

                }else if(trend_type == 'number'){

                    $(this).html('<span>'+td_name.split('')[td_name.split('').length == 2 ? 0 : 1]+'</span>').addClass('number');

                }else{
                    $(this).html('<span>'+td_name+'</span>').addClass('number');
                }

            });
            //重新绘制走势图
            app.render_trend();
            $('#trend-table td.td-active').removeClass('td-active');
        },
        render_trend : function(){
            $('#svg svg').remove();
            require(['raphael'],function(Raphael){
                var points = '', raphael = Raphael("svg", $('.svg').width(), $('.svg').height());
                $('.trend-table .number').each(function(){
                    points += ($(this).find('span').position().left+12)+','+($(this).find('span').position().top+12)+' ';
                });
                //绘制折线
                raphael.path('M '+points+'').attr({stroke: "#ffab0a"});
            });
        },
        //提示mark
        tipsmark : function(option){

            var tips_html = '<div class="tipsmark"><p>'+option.text+'</p></div>', left = 0, top = 0;

            $(option.obj).hover(function(){
                top = $(this).offset().top; left = $(this).offset().left;
                $('body').append(tips_html).find('.tipsmark').css({
                    'left' : left+option.left,
                    'top' : top+option.top
                });
            },function(){
                $('body').find('.tipsmark').remove();
            });

        },
        //加载mark
        loadmark : function(option){
            var settings={
                text     : '',
                time     : 1000,
                img      : false,
                callback : ''
            };
            $.extend( settings,option );
            $('body').find('.loadmark').remove();
            var load_gif='';
            if(settings.img){load_gif='<p class="load_gif"><img src="/static/images/icon/loading.gif" width="100%" /></p>';}
            var $loadHtml='<div class="loadmark"><div class="load_tip">'+load_gif+$.trim(settings.text)+'</div></div>';
            $('body').append($loadHtml);
            var $loadmark=$('body').find(".loadmark");
            $loadmark.find('.load_tip').css({
                'margin-left':-($loadmark.find('.load_tip').outerWidth()/2),
                'margin-top':-($loadmark.find('.load_tip').outerHeight()/2)
            });
            if(settings.time > 0){
                var time=null;
                var time=setTimeout(function(){
                    $loadmark.remove();
                    if(settings.callback!=''){settings.callback();};
                    clearTimeout(time);
                },settings.time);
            }
        },
        closemark : function(){
            $('.loadmark').remove();
        },
        get_date : function(format){
            var time = new Date();
            if (!time instanceof Date) return;
            var dict = {
                "yyyy" : time.getFullYear(),
                "M"    : time.getMonth() + 1,
                "d"    : time.getDate(),
                "H"    : time.getHours(),
                "m"    : time.getMinutes(),
                "s"    : time.getSeconds(),
                "MM"   : ("" + (time.getMonth() + 101)).substr(1),
                "dd"   : ("" + (time.getDate() + 100)).substr(1),
                "HH"   : ("" + (time.getHours() + 100)).substr(1),
                "mm"   : ("" + (time.getMinutes() + 100)).substr(1),
                "ss"   : ("" + (time.getSeconds() + 100)).substr(1)
            };
            return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {
                return dict[arguments[0]];
            });
        },
        format_time : function(data,tip){
            var time = parseFloat(data), h = 0, m = 0, s = 0, h_fg = '<em>时</em>', f_fg = '<em>分</em>', s_fg = '<em>秒</em>', span_t = '<span>', span_w = '</span>';
            if (time != null && time != ""){
                if (time < 60) {
                    s = time;
                    time =  span_t + '00' + span_w  + f_fg + span_t+ (s > 9 ? s : '0'+s) +span_w + s_fg;
                } else if (time >= 60 && time < 3600) {
                    m = parseInt(time / 60);
                    s = parseInt(time % 60);
                    time = span_t + (m > 9 ? m : '0'+m) + span_w + f_fg + span_t + (s > 9 ? s : '0'+s) + span_w + s_fg;
                } else if (time >= 3600) {
                    h = parseInt(time / 3600);
                    m = parseInt(time % 3600 / 60);
                    s = parseInt(time % 3600 % 60 % 60);
                    time = span_t + (h > 9 ? h : '0'+h) + span_w + h_fg + span_t + (m > 9 ? m : '0'+m) + span_w + f_fg; // + (s > 9 ? s : '0'+s) + s_fg;
                }
            }
            return time == 0 && tip ? tip : time;
        },
        //判断号码是通吃还是大小
        tc : function(sum,arr){
            if(app.unique_len(arr) == 1){
                return '通吃';
            }else{
                return sum > 10 ? '<span class="c-red">大</span>' : '小';
            }
        },
        //判断龍虎
        lh : function(a,b,red){

            var num_a = parseInt(a), num_b = parseInt(b);

            if(num_a > num_b){
                return red == 'l' ? '<span class="c-red">龍</span>' : '龍';
            }else if(num_a == num_b){
                return '和';
            }else{
                return red == 'h' ? '<span class="c-red">虎</span>' : '虎';
            }

        },
        //判断单双
        ds : function(val,red){

            if(val % 2 == 0){
                return red == 's' ? '<span class="c-red">双</span>' : '双';
            }else{
                return red == 'd' ? '<span class="c-red">单</span>' : '单';
            }

        },
        //判断大小
        dx : function(a,b,red){

            var num_a = parseInt(a), num_b = parseInt(b);

            if(num_a > num_b){
                return red == 'd' ? '<span class="c-red">大</span>' : '大';
            }else if(num_a == num_b){
                return '和';
            }else{
                return red == 'x' ? '<span class="c-red">小</span>' : '小';
            }

        },
        //返回去重之后数组的长度
        unique_len : function(arr){
            return $.unique(arr).length;
        },
        //判断豹子、对子、顺子、半顺、杂六
        shun : function (arr){

            var new_arr = arr.sort(app.sort), num = 0;

            if(app.unique_len(arr) == 1){
                return '豹子';
            }else if(app.unique_len(arr) == 2){
                return '对子';
            }else{

                for(var i = 0,len = new_arr.length -1 ; i < len; i++){
                    if(parseInt(new_arr[i])+1 == new_arr[i+1]){
                        num ++ ;
                    }
                }

                if(num == new_arr.length - 1){
                    return '顺子';
                }else if(num > 0){
                    return '半顺';
                }else{
                    return '杂六';
                }

            }
            
        },
        date_format:function(time,format){
            if (!time) return;
            if (!format) format = "yyyy-MM-dd";
            if(time.indexOf('-')>-1){
                time = time.replace(/-/g,'/');
            }
            switch(typeof time) {
                case "string":
                    time = new Date(time.replace(/-/g, "/"));
                    break;
                case "number":
                    time = new Date(time);
                    break;
            }
            if (!time instanceof Date) return;
            var dict = {
                "yyyy" : time.getFullYear(),
                "M"    : time.getMonth() + 1,
                "d"    : time.getDate(),
                "H"    : time.getHours(),
                "m"    : time.getMinutes(),
                "s"    : time.getSeconds(),
                "MM"   : ("" + (time.getMonth() + 101)).substr(1),
                "dd"   : ("" + (time.getDate() + 100)).substr(1),
                "HH"   : ("" + (time.getHours() + 100)).substr(1),
                "mm"   : ("" + (time.getMinutes() + 100)).substr(1),
                "ss"   : ("" + (time.getSeconds() + 100)).substr(1)
            };
            return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {
                return dict[arguments[0]];
            });
        },
        time : function(data){
            var time = parseFloat(data), h = 0, m = 0, s = 0, day = 0;
            if (time != null){
                if (time < 60) {
                    s = time;
                    time = '00:' + (s > 9 ? s : '0'+s);
                } else if (time >= 60 && time < 3600) {
                    m = parseInt(time / 60);
                    s = parseInt(time % 60);
                    time = (m > 9 ? m : '0'+m) + ':' + (s > 9 ? s : '0'+s);
                }else if(time >= 3600){
                    h = parseInt(time / 3600);
                    m = parseInt(time % 3600 / 60);
                    s = parseInt(time % 3600 % 60 % 60);
                    time = (h > 9 ? h : '0'+h) + ':' + (m > 9 ? m : '0'+m) + ':' + (s > 9 ? s : '0'+s);
                }
            }
            return time;
        }
    }

});
