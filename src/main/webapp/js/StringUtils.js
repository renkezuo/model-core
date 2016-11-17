String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);
    }
}

/**
 * Created by Administrator on 13-12-20.
 */
NullUtils = {
    NULL: "NULL",
    isNotNull: function(o) {
        return o != null && o != undefined;
    },
    isNull: function(o) {
        return !this.isNotNull(o);
    }
}

StringUtils = {
    /**
     * 限制字符
     */
    RESTRICT:"<>︻︼︽︾〒↑↓☉⊙●〇◎¤★☆■▓「」『』◆◇▲△▼▽◣◥◢◣◤◥№↑↓→←Ψ※㊣∑⌒∩【】〖〗＠ξζω□∮〓※》∏卐√╳々♀♂∞①ㄨ≡╬╭╮╰╯╱╲▂▂▃▄▅▆▇█▂▃▅▆█▁▂▃▄▅▆▇█▇▆▅▄▃▂▁,",
    INPUT_WORLD_HERE: "input words here",
    UNKNOWN:"unknown",
    SYSTEM:"system",

    /**
     * 改名卡 验证名字是否有效
     * @param world
     * @return 1-验证通过
     * 其他不通过
     */
    checkIsValidChangeName: function(word){
        var regular = /^([\u0600-\u06ff]|[\u0750-\u077f]|[\ufb50-\ufc3f]|[\ufe70-\ufefc]|[a-zA-Z0-9]|\s){3,10}$/;
        if(StringUtils.isBlank(word)) {
            return -1;
        }
        if(word.charAt(0) == " " || word.charAt(word.length - 1) == " ") {
            return -1;
        }

        if(regular.test(word)) {
            return 1;
        } else {
            return -2;
        }
    },

    checkContainRestrict: function(string){
        for(var idx in this.RESTRICT) {
            string = string.replace(this.RESTRICT[idx], "");
        }

        return string;
    },
    isBlank: function(s) {
        if(!s) {
            return true;
        }

        /*if(!isNaN(s)) { //是一个数字
            return false;
        }

        if((typeof s) != "string") {
            return false;
        }*/

        if(s == "" || s == this.INPUT_WORLD_HERE || (s.match && s.match(/^\s+$/g))) {
            return true;
        }

        return false;
    },

    isNotBlank: function(s){
        return !this.isBlank(s);
    },

    equals: function(s1, s2) {
        return s1 == s2;
    },

    equalsIgnoreCase: function(s1, s2){
        if(s1 == s2) {
            return true;
        }

        if(NullUtils.isNull(s1) || NullUtils.isNull(s2)) {
            return false;
        }

        return s1.toLocaleUpperCase() == s2.toLocaleUpperCase();
    },

    subString: function(s, start, len) {
        if(this.isBlank(s)) {
            return s;
        }

        /*var end = start + len;

        if(s.length < end) {
            end = s.length;
        }*/

       return s.substr(start, len);
    },

    /**
     * 过滤掉html标签
     * @param str
     * @returns {string}
     */
    removeHTMLTag: function(str) {
        str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
        str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
        //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
        str=str.replace(/&nbsp;/ig,'');//去掉&nbsp;
        return str;
    }
}

CollectionUtils = {
    isEmpty: function(o) {
        if(! o) return true;

        var list = null;
        if(o instanceof  Array) {
            list = o;
        } else if(o.list) {
            list = o.list;
        } else if(o.items) {
            list = o.items;
        }
        //后续继续

        if(!list || list.length < 1) {
            return true;
        }

        return false;
    },

    isNotEmpty: function(o) {
        return !this.isEmpty(o);
    },
    removeIndex: function(array,index) { // array.splice(index, 1)
        var pop = array.splice(index, 1);
        return pop;
    }
}

NumberUtils = {
    /**
     * 向上取整
     */
    ceil: function(s){
        var v = Math.ceil(s);
        if(isNaN(v)) {
            return -1;
        }

        return v;
    },
    toInt: function(s) {
        return RP.Tool.strToInt(s);
    },

    isNumber: function(o) {
        if(o instanceof Number) {
            return true;
        }
        return NumberUtils.toInt(o) == o
    },

    toLocaleString: function(num) {
        if(num < 1000) {
            return num + "";
        }

        var retV = "";
        var wasContinue = true;
        var tempV = 0;
        while(wasContinue == true) {
            tempV = num%1000;
            num = this.toInt(num/1000);
            wasContinue = num >= 1;

            if(wasContinue) {
                if(tempV < 10) {
                    tempV = "00" + tempV;
                }else if(tempV < 100) { //少于三位数 补0
                    tempV = "0" + tempV;
                }
            }
            retV = tempV + retV;

            if(wasContinue) {
                retV = "," + retV;
            }
        }

       return retV;
    },

    getNumberFromLocaleString: function(stringValue) {
        return this.toInt(stringValue.replaceAll(",", ""));
    }
}

BooleanUtils = {
    parseBooleanWithDefault: function(o, defaultValue) {
        if(o == null || o == undefined) {
            return defaultValue;
        }

        return this.parseBoolean(o);
    },
    parseBoolean: function(o) {
        if(o == null || o == undefined) {
            return false;
        }

        if(o instanceof  Number) {
            return o != 0;
        }

        if(o == "true" || o == "TRUE" || o == true)
            return true;

        return false;
    }
}

// 表单验证
var TextUtil = {
		
	isPhone: function(phone) {
		var myreg = /^\d{11}$/;
		if(!myreg.test(phone)) { 
		    return false; 
		} 
		
		return true;
	},
    /*
    * 是否邮件地址
    * */

    isEmail: function(email) {
        var res = email.match(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/);
        if (res !== null) {
            return res[0];
        }
        return false;
    },
    /**
     * 是否仅数字和字母
     * @param str
     * @param min 最小长度 默认0
     * @param max 最大长度 默认最大10000
     * @returns {boolean}
     */
    isNumberCharacter: function (str, min, max) {
        min = min || 1;
        max = max || 10000;
        var len = str.length;
        if (min > len || len > max) {
            return false;
        }
        var res = new RegExp(/^[a-z0-9]+$/i);
        return res.test(str);
    },
    /**
     * 仅允许非特殊字符
     * @param str
     * @param min
     * @param max 10000
     * @returns {boolean}
     */
    isCharacter: function(str, min, max) {
        min = min || 1;
        max = max || 10000;
        var len = str.length;
        if (min > len || len > max) {         // 长短限制
            return false;
        }
        if (new RegExp(/(^\s)|(\s$)/).test(str)) {// 首尾不允许有空格
            return false;
        }
        var patten = new RegExp(/[`~!@#$%^&*()_+<><>︻︼︽︾〒↑↓☉⊙●〇◎¤★☆■▓「」『』◆◇▲△▼▽◣◥◢◣◤◥№↑↓→←Ψ※㊣∑⌒∩【】〖〗＠ξζω□∮〓※》∏卐√╳々♀♂∞①ㄨ≡╬╭╮╰╯╱╲▂▂▃▄▅▆▇█▂▃▅▆█▁▂▃▄▅▆▇█▇▆▅▄▃▂▁,?:"{},.\/;'[\]]/im);
        return !patten.test(str);
    },

    /**
     * 账号校验
     * @param strAcc
     * @returns {boolean}
     */
    isAccount: function(strAcc) {
        if (this.isEmail(strAcc)) {
            return true;
        }
        return this.isCharacter(strAcc, 3,10);
    },
    isPwd: function(strPwd) {
        return this.isNumberCharacter(strPwd, 6,18);
    },

    /**
     * 是否URL格式
     * @param str
     * @returns {*}
     */
    isHttp: function(str) {
        var regHttp = /^https?:\/\/(([a-zA-Z0-9_-])+(\.)?)*(:\d+)?(\/((\.)?(\?)?=?&?[a-zA-Z0-9_-](\?)?)*)*$/i;
        var resRegHttp = str.match(regHttp);

        if (resRegHttp) {
            return resRegHttp[0];
        }
        return false;
    }
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.format = function(fmt) { //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

var DateUtils = {
    formatDate: function(dateTimeStamp){
        if(!dateTimeStamp || dateTimeStamp <= 0) {
            return "1970-01-01 00:00:00";
        }
        return new Date(dateTimeStamp).format("yyyy-MM-dd hh:mm:ss");
//
//        var time2 = new Date().format("yyyy-MM-dd");
//
//        return new Date(parseInt(dateTimeStamp)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
    },

    formatDate2: function(dateTimeStamp){
        if(!dateTimeStamp || dateTimeStamp <= 0) {
            return "1970-01-01";
        }
        return new Date(dateTimeStamp).format("yyyy-MM-dd");
//
//        var time2 = new Date().format("yyyy-MM-dd");
//
//        return new Date(parseInt(dateTimeStamp)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
    },
    _format:function(leftTime){
        if(leftTime <= 0) {
            return null;
        }
        var leftSecond = parseInt(leftTime/1000);
        var day = Math.floor(leftSecond/(60*60*24));
        var hour = Math.floor((leftSecond-day*24*60*60)/3600);
        var minute = Math.floor((leftSecond-day*24*60*60-hour*3600)/60);
        var second = Math.floor(leftSecond-day*24*60*60-hour*3600-minute*60);

        return {leftSecond:leftSecond,day:day, hour:hour,minute:minute,second:second};
    },

    formatTimeDiff2:function(leftTime){},
    formatTimeDiff: function(leftTime){
        var o = this._format(leftTime);
        if(o == null) {
            return "-- : --";
        }

        var day = o.day;
        var hour = o.hour;
        var minute = o.minute;
        var second = o.second;

        var s = "";
        if(day < 10) {
            if(day != 0) {
                s += "0" + day + "D "
            }
        } else {
            s += day + "D "
        }

        if(hour < 10) {
            s += "0" + hour + ":"
        } else {
            s += hour + ":"
        }


        if(minute < 10) {
            s += "0" + minute;
        } else {
            s += minute;
        }

        if(day < 1) {
            s += ":";
            if(second < 10) {
                s += "0" + second;
            } else {
                s += second
            }
        }


        return s;
    }
}

ObjUtils = {
		toString : function(o) {
			if(NullUtils.isNull(o)) {
				return "null";
			}
			
			if((typeof o) == "string") {
				return o;
			} else if((typeof o) == "object") {
				var s = "";
				for(var idx in o) {
					s += idx + "=" + ObjUtils.toString(o[idx]) + ",";
				}
				
				return s;
			} else if((typeof o) == "funtion") {
				var s = "fun[" + o +"]";
			} else {
				return o;
			}
		}
}