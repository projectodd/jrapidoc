/**
 * Created by Tomas "sarzwest" Jiricek on 21.4.15.
 */
var Logger = function() {
};

Logger.error = function(msg) {
    msg = "Error occured! \n" + msg;
    $.notify(msg, "error");
    console.log(msg);
};

Logger.success = function(msg) {
    $.notify(msg, "success");
    console.log(msg);
};