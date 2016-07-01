// FIRST, EXECUTE ss.sc FILE:
// .....


// SECOND... RUN THIS TWICE... WHY TWICE???????
(
~ss.start(
	~ss.loadCommon(
		{
~me = NetAddr("127.0.0.1", 57120);
~controlBusBack = Bus.control(s,1);
OSCdef(\listenBack, func:{arg msg;
	~controlBusBack.value = msg[1];
}, path:'/pineapple/back');

~controlBusBackCount = Bus.control(s,1);
OSCdef(\listenBackCount, func:{arg msg;
	~controlBusBackCount.value = msg[1];
}, path:'/pineapple/back/count');

~controlBusForward = Bus.control(s,1);
OSCdef(\listenForward, func:{arg msg;
	~controlBusForward.value = msg[1];
}, path:'/pineapple/forward');

~controlBusForward8 = Bus.control(s,1);
OSCdef(\listenForward8, func:{arg msg;
	~controlBusForward8.value = msg[1];
}, path:'/pineapple/forward/8');

~controlBusForward12 = Bus.control(s,1);
OSCdef(\listenForward12, func:{arg msg;
	~controlBusForward12.value = msg[1];
}, path:'/pineapple/forward/12');

~controlBusForward16 = Bus.control(s,1);
OSCdef(\listenForward16, func:{arg msg;
	~controlBusForward16.value = msg[1];
}, path:'/pineapple/forward/16');

		}
	)
);

~me.sendMsg('/pineapple/forward', 0);
~me.sendMsg('/pineapple/forward/8', 0);
~me.sendMsg('/pineapple/forward/12', 0);
~me.sendMsg('/pineapple/forward/16', 0);
~me.sendMsg('/pineapple/back', 0);
~me.sendMsg('/pineapple/back/count', 1);
)

// THIRD RUN THIS:

(
SynthDef("ss.pops8", {
	var times = 8;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward8), 2);
	Out.ar(~ss.bus.master,
		Splay.ar(
			{|i|
				r=i+1/8;
				freq = r/(i+2*3);
				Formlet.ar(
					Impulse.ar(r),
					LFSaw.ar(freq,1)+2*3**4,
					0.01,1/r/2)
			}!times, 0.4
		)*mul;
	);
}
).add;

SynthDef("ss.pops12", {
	var times = 12;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward12), 2);
	Out.ar(~ss.bus.master,
		Splay.ar(
			{|i|
				r=i+1/8;
				freq = r/(i+2*3);
				Formlet.ar(
					Impulse.ar(r),
					LFSaw.ar(freq,1)+2*3**4,
					0.01,1/r/2)
			}!times, 0.4
		)*mul;
	);
}
).add;

SynthDef("ss.pops16", {
	arg times = 16;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward16), 2);
	Out.ar(~ss.bus.master,
		Splay.ar(
			{|i|
				r=i+1/8;
				freq = r/(i+2*3);
				Formlet.ar(
					Impulse.ar(r),
					LFSaw.ar(freq,1)+2*3**4,
					0.01,1/r/2)
			}!12, 0.4
		)*mul;
	);
}
).add;

SynthDef("ss.bloo", {
	var temp, sum=0, numBloo=24;

	var mul = Lag.kr(In.kr(~controlBusBack), 2);
	var freq = Lag.kr(90*(1.5**In.kr(~controlBusBackCount)), 3);

	numBloo.do{
		arg count;
		temp = SinOsc.ar(
			freq
			* LFNoise2.kr({Rand(0.2, 4)}!2).exprange(0.9, 1.1)
			* (count + 1),
			mul: 1 / ((count+1)/2)
		);
		temp = temp * LFNoise2.kr({Rand(0.5, 4)}!2).exprange(0.01, 1);
		sum = sum + temp;
	};
	sum = (sum / numBloo) * 0.8;
	Out.ar(~ss.bus.master, sum*mul);
}).add;

SynthDef("ss.ringy", {arg freq=440;
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	Out.ar(~ss.bus.master,
        Pan2.ar(tone*0.6, 0)
    )
}).add;
)

// FINALLY... start the synths...
(
b = Synth("ss.bloo");
~p8 = Synth("ss.pops8");
~p12 = Synth("ss.pops12");
~p16 = Synth("ss.pops16");
)


r = Synth("ss.ringy");
r.stop;






// TESTS ---------------------------------------------

// TEST MSG SENDS:
~me.sendMsg('/pineapple/forward', 0);
~me.sendMsg('/pineapple/back', 0);


// TEST control from max
p = { In.kr(~controlBusBackCount).poll }; // to see the
p.stop;
p.play;