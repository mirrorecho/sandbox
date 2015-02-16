// synth definitions...
(
//var baseFreq = 79;

// ----------------------------------------------------------------------------------------------------------
// Synth def to loop sound file:

SynthDef(\trainLoop,{ arg out=0, rate=1, fadeIn=2, sustain=4, fadeOut=2, mul=1;

	var fileName = "/media/randall/SP PHD U3/Archive/2014-05-03 Handheld Recordings Download/DR0000_0130.wav";

	var bufSampleRate = SoundFile(fileName).sampleRate;

	var startOn = 11;
	var endOn = 13;
	var length = endOn - startOn;

	// NOTE... better off to use cue (or some other method for SoundFile instead of re-reading the sound file)
	var soundBuf = Buffer.new;

	soundBuf.allocRead(fileName, bufSampleRate*startOn, bufSampleRate*length);

	Out.ar(out,
		PlayBuf.ar(
			numChannels:2,
			bufnum:soundBuf.bufnum,
			rate:BufRateScale.kr(soundBuf.bufnum)*rate,
			loop:1)
		// dividing by rate is important to adjust circle to any possible rate...
		* EnvGen.ar(Env.circle([0,1,0], [length/(2*rate), length/(2*rate), 0]))
		* EnvGen.ar(Env.linen(fadeIn, sustain, fadeOut),doneAction:2)
		* mul
		,0.0);
}).add;

SynthDef(\siny, {arg out=0, freq=440, fadeIn=2, sustain=4, fadeOut=2, mul=0.6;
	var outArray = [SinOsc.ar(freq, 0, mul/4), SinOsc.ar(freq * 1.001, 0, mul/4)];
	Out.ar(out,
		outArray
		* EnvGen.ar(Env.linen(fadeIn, sustain, fadeOut),doneAction:2)
		* mul
		,0.0);
}).add;

SynthDef(\noiseSwell, {arg out=0, fadeIn=2, fadeOut=1, mulPeak=0.6, mulAfter=0.02;
	// note... would be cool to try band-limiting this?
	var outArray1 = [
		WhiteNoise.ar(1/4),
		WhiteNoise.ar(1/4)
	];
	var outArray2 = [
		PinkNoise.ar(1/6),
		PinkNoise.ar(1/6)
	];
	Out.ar(out,
		outArray1
		* EnvGen.ar(Env([0.00000001, mulPeak, mulAfter, 0.00000001], [fadeIn, 0.01, fadeOut], \exp), doneAction:2)
		+ outArray2
		* EnvGen.ar(Env([0, mulPeak/2, mulAfter, 0.00000001], [fadeIn, 0.01, fadeOut], \lin), doneAction:2)
		,0.0);
}).add;

)

(
Routine.run({
	Synth(\noiseSwell, [
		\fadeIn, 4,
		\fadeOut, 1
	]);

	4.wait;

	Synth(\siny, [
		\freq, 369.99 * 2,
		\fadeIn, 0.001,
		\sustain, 2,
		\fadeOut, 8,
		\mul, 0.2
	]);
});

)



(
Synth(\siny, [
	\freq, 369.99,
	\fadeIn, 4,
	\sustain, 8,
	\fadeOut, 8
]);
)




// run it!
(

var length = 2;

var baseFrequency = 369.99;

var trainDrone = { arg rate=1, fadeIn, sustain, fadeOut, mul;
	Task.new({
		Synth(\trainLoop, [
			\rate, rate,
			\fadeIn, fadeIn,
			\sustain, sustain,
			\fadeOut, fadeOut,
			\mul: mul
		]);

		(length/(rate*2)).wait;

		Synth(\trainLoop, [
			\rate, rate,
			\fadeIn, fadeIn,
			\sustain, sustain,
			\fadeOut, fadeOut,
			\mul, mul
		]);
	});
};

Routine.run({

	Synth(\siny, [
	\freq, baseFrequency,
	\fadeIn, 8,
	\sustain, 8,
	\fadeOut, 8,
	\mul, 0.2
	]);

	trainDrone.value( rate:1, fadeIn:16, sustain:66, fadeOut:20, mul:0.2 ).play;
	4.wait;
	trainDrone.value( rate:1.01, fadeIn:16, sustain:60, fadeOut:20, mul:0.2 ).play;
	trainDrone.value( rate:1.02, fadeIn:16, sustain:60, fadeOut:20, mul:0.2 ).play;
	trainDrone.value( rate:2/3, fadeIn:8, sustain:60, fadeOut:20, mul:0.3 ).play;
	12.wait;
	trainDrone.value( rate:9/8, fadeIn:3, sustain:5, fadeOut:3, mul:0.6 ).play;

	4.wait;
	trainDrone.value( rate:2, fadeIn:3, sustain:5, fadeOut:3, mul: 0.4 ).play;

	4.wait;

		Synth(\siny, [
	\freq, baseFrequency * 15/16 * 12,
	\fadeIn, 8,
	\sustain, 4,
	\fadeOut, 12,
	\mul, 0.08
	]);

	Synth(\siny, [
	\freq, baseFrequency * 15/16 * 12.01,
	\fadeIn, 8,
	\sustain, 4,
	\fadeOut, 12,
	\mul, 0.08
	]);

	trainDrone.value( rate:15/16 * 6, fadeIn:5, sustain:2, fadeOut:2, mul: 0.06 ).play;
	2.wait;
	trainDrone.value( rate:15/16 * 4, fadeIn:3, sustain:2, fadeOut:2, mul: 0.06 ).play;


	4.wait;
	trainDrone.value( rate:4, fadeIn:3, sustain:2, fadeOut:2, mul: 0.2 ).play;
	trainDrone.value( rate:6, fadeIn:3, sustain:2, fadeOut:2, mul: 0.2 ).play;

	1.wait;
	trainDrone.value( rate:8, fadeIn:3, sustain:2, fadeOut:16, mul: 0.12 ).play;
	4.wait;
	trainDrone.value( rate:16, fadeIn:3, sustain:2, fadeOut:16, mul: 0.01 ).play;

	6.wait;

	Synth(\siny, [
	\freq, baseFrequency * 2/3 * 1.01,
	\fadeIn, 6,
	\sustain, 8,
	\fadeOut, 12,
	\mul, 0.01
	]);
	Synth(\siny, [
	\freq, baseFrequency * 1/3,
	\fadeIn, 6,
	\sustain, 8,
	\fadeOut, 12,
	\mul, 0.01
	]);


	8.wait;
	trainDrone.value( rate:1/3, fadeIn:8, sustain:12, fadeOut:4, mul:0.8 ).play;

	trainDrone.value( rate:1/6, fadeIn:8, sustain:12, fadeOut:4, mul:0.9 ).play;
	trainDrone.value( rate:(1/6)*1.01, fadeIn:8, sustain:12, fadeOut:4, mul:0.9 ).play;
	1.wait;
	trainDrone.value( rate:8/9, fadeIn:3, sustain:5, fadeOut:3, mul: 0.5 ).play;



});
)


