/**
 * Created by Tomas "sarzwest" Jiricek on 21.4.15.
 */
var CaughtException = function (msg){
    this.msg = msg;
};

CaughtException.prototype.getMsg = function(){
    return this.msg;
};