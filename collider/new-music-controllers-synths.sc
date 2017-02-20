// NEW SYNTHS FOR CLUB NIGHT
(
SynthDef("ss.saw", {
	CombC.ar(Saw.ar(LFPulse.ar(1!2)*f+f+(LFPulse.ar(y/9)*f+g)+(LFPulse.ar(y)*f)+(LFPulse.ar(1/y)*g)+(LFPulse.ar(y*2)*g))*(6-y)/22)}
}
).add;
)




(
SynthDef("ss.pops8", {
	var times = 4;
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
	var times = 24;
	var freq,r;
	var mul = Lag.kr(In.kr(~controlBusForward16), 4);
	Out.ar(~ss.bus.master,
		Splay.ar({|i|
			Ringz.ar(
				Decay.ar(
					Impulse.ar(r=i+1/4),1/r,
					Crackle.ar/6),
				LFSaw.ar(f=r/(i+2*3),1)+2*3**4,f)}!times,0.4)*mul;
	);
}
).add;

SynthDef("ss.bloo", {
	var temp, sum=0, numBloo=12;

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

SynthDef("ss.ringy", {
	var counter = Lag.kr(In.kr(~controlBusCounter), 0.1);
	var counterControl = Lag.kr(In.kr(~controlBusForward16) + In.kr(~controlBusForward8), 0.1);

	var freq=45*( (9.5/8) **counter);
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	var ghosts = Resonz.ar(Crackle.ar(1.98!2), freq, 0.04, 12) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 6) +
	Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 4) +
	Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 2) +
	Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 1);

	Out.ar(~ss.bus.master,
		(Pan2.ar(tone*0.1, 0) + ghosts*0.3 ) * 0.2 * counterControl
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


)