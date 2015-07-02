/**
 * Created by Tomas "sarzwest" Jiricek on 21.4.15.
 */
var ProgressBar = function(){

};

ProgressBar.showProgressBar = function() {
    document.querySelector("#progress").style.display = "block";
};

ProgressBar.hideProgressBar = function() {
    document.querySelector("#progress").style.display = "none";
};