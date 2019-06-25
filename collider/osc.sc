
// run ifconfig | grep inet to get IP address

(
~controlBus = Bus.control(s,1);
// NOTE: don't use 57110... it's a special-use server port
// n = NetAddr("192.168.1.32", 8000);
n = NetAddr("127.0.0.1", 8000);
// n = NetAddr("10.0.104.33", 8000);
)



(
{ In.kr(~controlBus).poll }.play; // to see the
)

(
{
	var freq;
	freq = Lag.kr(In.kr(~ss.bus.accelX).linexp(-20, 20, 80, 4000), 0.04);
	SinOsc.ar(freq:freq, mul:0.6)
}.play;
)


)

(

OSCdef(\listen1, func:{arg msg;
	var freq, sig;
	msg.postln;
	// freq = Line.kr(msg[1], msg[2], msg[3], doneAction:2);
	// sig = Saw.ar(freq:freq * [0.99, 1.01], mul: 0.1);
	// LFnoise1.kr ...

},
// path:'/accelerometer/raw/x', recvPort:8000);
path:'/streamo', recvPort:8000);

)


// OSC msgs with control bus
// receiving:
(
OSCdef(\listen1, func:{arg msg;
	// msg[2].postln;

	~controlBus.value = msg[2];
	// msg.postln;
}, path:'/streamo');
)

