(

SynthDef(\KSpluck, { arg midiPitch = 69, delayDecay = 0.5;

	var burstEnv, att = 0, dec = 0.0001;

	var signalOut, delayTime;


	delayTime = [midiPitch, midiPitch + 12].midicps.reciprocal;

	burstEnv = EnvGen.ar(Env.perc(att, dec));

	signalOut = WhiteNoise.ar(burstEnv);
	signalOut = BPF.ar(signalOut, midiPitch.midicps);

	signalOut = CombL.ar(signalOut, delayTime, delayTime, delayDecay, add: signalOut);

	DetectSilence.ar(signalOut, doneAction:2);

	Out.ar(0, signalOut)

	}

).store;

)



(

//Then run this playback task

r = Task({

	{Synth(\KSpluck,

		[

			// \midiPitch, rrand(30, 90), //Choose a pitch

		\delayDecay, rrand(0.5, 3.0) //Choose duration

		]);

		//Choose a wait time before next event

		[0.125, 0.125, 0.25].choose.wait;

	}.loop;

}).play

)