// NEW SYNTHS FOR CLUB NIGHT
(
~popsDef = { arg respondTo, times;
	SynthDef("ss.pops." ++ respondTo, {
		// var times=4, respondTo=1;
		// var mul = Lag.kr(In.kr(~controlBusForward8), 2);
		var forwardCount = In.kr(~controlBusCounter) * In.kr(~controlBusForward);
		// var forwardCount = 1;
		var trigger = BinaryOpUGen('==', respondTo, forwardCount);
		var mul = Lag.kr(trigger, 2);
		var freq, r;
		var sig = Splay.ar(
			{|i|
				r=i+1/8;
				freq = r/(i+2*3);
				Formlet.ar(
					Impulse.ar(r),
					LFSaw.ar(freq,1)+2*3**4,
					0.01,1/r/2)
			}!times, 0.4
		)*mul;
		Out.ar(~ss.bus.master, sig);
	}
	).add;
};

// WHY DOESN'T THIS WORK???? ...
// {|i|
// 	~popsDef.value(i,i);
// }!16;

~popsDef.value(1,1);
~popsDef.value(2,2);
~popsDef.value(3,3);
~popsDef.value(4,4);
~popsDef.value(5,5);
~popsDef.value(6,6);
~popsDef.value(7,7);
~popsDef.value(8,8);
~popsDef.value(9,9);
~popsDef.value(10,10);
~popsDef.value(11,11);
~popsDef.value(12,12);
~popsDef.value(13,13);
~popsDef.value(14,14);
~popsDef.value(15,15);
~popsDef.value(16,16);
~popsDef.value(17,17);
~popsDef.value(18,18);
~popsDef.value(19,19);
~popsDef.value(20,20);
~popsDef.value(21,21);
~popsDef.value(22,22);
~popsDef.value(23,23);
~popsDef.value(24,24);

SynthDef("ss.bloo", {
	var temp, sum=0, numBloo=12;

	var mul = Lag.kr(In.kr(~controlBusBack), 2);
	// var mul = 1;

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

SynthDef("ss.ringy", {
var counter = Lag.kr(In.kr(~controlBusCounter), 0.1);

		var forwardCount = In.kr(~controlBusCounter);
	var isForward = In.kr(~controlBusForward);
	var mulTrigger = (forwardCount <= 24) * isForward;


	var freq=45*( (9.5/8) **counter);
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	var ghosts = Resonz.ar(Crackle.ar(1.98!2), freq, 0.04, 12) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 6) +
	Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 4) +
	Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 2) +
	Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 1);

	Out.ar(~ss.bus.master,
		(Pan2.ar(tone*0.1, 0) + ghosts*0.3 ) * 0.2 * mulTrigger;
		)
	}).add;


SynthDef("ss.mower",{ arg out=0, rate=1, fadeIn=1, sustain=1, fadeOut=1;

	var fileName = "/Users/rwest/Downloads/Lawn mower short clip 2.wav";
	var mul = Lag.kr(In.kr(~controlBusMower), 12);

	var bufSampleRate = SoundFile(fileName).sampleRate;

	var startOn = 0;
	var endOn = 3;
	var length = endOn - startOn;

	// NOTE... better off to use cue (or some other method for SoundFile instead of re-reading the sound file)
	var soundBuf = Buffer.new;

	soundBuf.allocRead(fileName, bufSampleRate*startOn, bufSampleRate*length);
		z = PlayBuf.ar(
			numChannels:2,
			bufnum:soundBuf.bufnum,
			rate:BufRateScale.kr(soundBuf.bufnum)*rate,
		loop:1) * EnvGen.ar(Env.circle([0,1,0], [length/(2*rate), length/(2*rate), 0]));
	Out.ar(~ss.bus.master,
		// z
		// dividing by rate is important to adjust circle to any possible rate...
		DelayN.ar(z, length/(2*rate), length/(2*rate), 1, z)
		 + Crackle.ar(1.98, 0.1)
		// * EnvGen.ar(Env.linen(fadeIn, sustain, fadeOut),doneAction:2)
		* mul * 0.6
		,0.0);
}).add;


SynthDef("ss.cicadas",{ arg out=0, rate=1, fadeIn=1, sustain=1, fadeOut=1;

	var fileName = "/Users/rwest/Documents/Sound/2016-Japan/DR0000_0159.wav";
	// var mul = Lag.kr(In.kr(~controlBusMower), 12);
	var forwardCount = In.kr(~controlBusCounter);
	var isForward = In.kr(~controlBusForward);
	var mulTrigger = (forwardCount <= 24) * isForward;
	// var mulOffTrigger = (forwardCount > 24);

	// var mulDenom = (26 * mulTrigger + mulOffTrigger );
	// var mul = Lag.kr( ( (1/mulDenom) + 0.2) * mulTrigger, 2);
	var mul = Lag.kr( forwardCount/25 * 0.4 ) * mulTrigger;

	var bufSampleRate = SoundFile(fileName).sampleRate;

	var startOn = 0;
	var endOn = 3;
	var length = endOn - startOn;

	// NOTE... better off to use cue (or some other method for SoundFile instead of re-reading the sound file)
	var soundBuf = Buffer.new;

	soundBuf.allocRead(fileName, bufSampleRate*startOn, bufSampleRate*length);
		z = PlayBuf.ar(
			numChannels:2,
			bufnum:soundBuf.bufnum,
			rate:BufRateScale.kr(soundBuf.bufnum)*rate,
		loop:1) * EnvGen.ar(Env.circle([0,1,0], [length/(2*rate), length/(2*rate), 0]));
	Out.ar(~ss.bus.master,
		// z
		// dividing by rate is important to adjust circle to any possible rate...
		DelayN.ar(z, length/(2*rate), length/(2*rate), 1, z)
		// + Crackle.ar(1.98, 0.1)
		// * EnvGen.ar(Env.linen(fadeIn, sustain, fadeOut),doneAction:2)
		* mul
		,0.0);
}).add;


)