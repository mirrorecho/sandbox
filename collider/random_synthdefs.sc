(
SynthDef(\ringy, {arg freq=440;
	var tone = SinOsc.ar(freq) * Pulse.ar(freq*1.4, mul:0.1);
	Out.ar(0,
        Pan2.ar(tone*0.6, 0)
    )
}).add;
)

(
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

Synth(\noiseSwell);
Synth(\ringy, [freq:880]);


