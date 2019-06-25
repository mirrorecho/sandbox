
(

SynthDef("ss.ringme", { arg attackTime=0.001, releaseTime=4, curve= -4, amp=1,
	freq=440,
	bufnum=~ss.buf['japan-cicadas']['0159-insects-in-kyoto'], rate=1, start=0,
	out=~ss.bus.master;

	var sig, bufsig, specsArray, env;

	bufsig = PlayBuf.ar(2,
		bufnum:bufnum,
		rate:BufRateScale.kr(bufnum)*rate,
		startPos:BufSampleRate.kr(bufnum) * start,
		doneAction:2,
		);

	specsArray = [
		[1, 2, 3, 4, 5, 6, 7, 8]*freq, // FREQS
		[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1], // AMPLITUDES
		[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1] // RING TIMES
	];

	env = EnvGen.ar(Env.perc(attackTime, releaseTime, level:amp, curve:curve), doneAction:2);

	// sig = Klank.ar(`specsArray, bufsig);
	sig = Klank.ar(`specsArray, bufsig) * AmpComp.kr(freq, 90, 0.6);

	sig = sig * env;

	Out.ar(out, sig);

}).add;

)

(
~b1 = ~ss.buf['japan-cicadas']['0159-insects-in-kyoto'];
~b2 = ~ss.buf['japan-cicadas']['0185-insects-water-kyoto'];
~b3 = ~ss.buf['japan-cicadas']['0191-insects-temple'];
~b4 = ~ss.buf['japan-cicadas']['0192-insects-honen-in-temple'];
)

Synth("ss.ringme", [buffer:~b1, releaseTime:]);
Synth("ss.ringme", [buffer:~b1, freq:880]);1
Synth("ss.ringme", [buffer:~b2]);
Synth("ss.ringme", [buffer:~b2, freq:880, releaseTime:1]);
Synth("ss.ringme", [buffer:~b3]);
Synth("ss.ringme", [buffer:~b4]);
~b4.play;

AmpComp

100*[1, 2, 3, 4, 5, 6, 7.5, 8];

(

var initSpecsArray = [
	[1, 2, 3, 4, 5, 6, 7, 8], // FREQ MULTIPLIERS
	nil, // AMPLITUDES
	[1, 1, 1, 1] // RING TIMES
];

var specsArray = initSpecsArray;
specsArray[0]=specsArray[0].collect{|m|400*m;};

initSpecsArray.postln;
specsArray.postln;


)