var exec = require('cordova/exec');

var VideoCapturePlugin = {
    captureVideo: function(success, error) {
        exec(success, error, "VideoCapturePlugin", "captureVideo", []);
    }
};

module.exports = VideoCapturePlugin;
