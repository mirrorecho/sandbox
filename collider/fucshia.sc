//  THE POINT: CLUSTERS OF SCALE DEGREES RANDOM CLOUDS (pulsing)
(
SynthDef( \pop, {
	arg freq=440, gate=1, amp=1.0, slideTime = 1.0;
	var sig, sig2, env;
	freq = Lag.kr(freq, slideTime);
	sig = Resonz.ar(WhiteNoise.ar(1.98!2), freq, 0.04, 22) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 22);
	sig = sig * amp * 8;
	sig2 = Splay.ar(sig, spread:0.9);
	sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.4);
	env = EnvGen.kr(Env.perc, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(0, sig2);
}).add;
)

(
Pbind(*[
	instrument: \pop,
	degree:Pwhite(-2,22),
	dur:(0.4)
]
).play(quant:0.4);
)

(
Pbind(*[
	instrument: \pop,
	amp:2,
	degree:Pwhite(-12, -4),
	dur:(2)
]
).play(quant:0.4);
)

(
Pbind(*[
	instrument: \pop,
	degree:Pwhite(28,34),
	dur:(1.2)
]
).play(quant:0.4);
)