(
p = Pbind(*[
	instrument: \default,
	detune: Prand( [ [0,1,3], [0,2,3], [0,1,2],], inf),
	freq: Pseq( [[1,1.5,2],[2,4],[3,4],[(15/16*4),4],[5,6],6,8,9,10,12,15,16] * 90, inf ),
	db: Pseq( [-33, -30, -28, -22], inf),
	pan: Pseq( [-0.6, -0.2, 0.6, -0.2], inf ),
	dur: Prand( [0.2, 0.2, 0.2, 0.2, 0.4, 0.4, 0.8], inf ),
	legato: Pseq( [2, 0.5, 0.75, 0.5, 0.25], inf )
]);
)
// ------------------------------------------------------
(
q = Pbind(*[
	stretch: Pseg([0, 0.1, 0.2, 1],8).linexp(0,1,1,0.125),
	midinote: 100.cpsmidi,
	harmonic: Pwhite(1,16),
	legato: Pkey(\stretch) * Pkey(\harmonic) / 2,
	db: -10 - Pkey(\harmonic),
	detune: Pwhite(0.0,3.0),
	dur: 0.2,
])
)
// ------------------------------------------------------
(
a = Pbind(*[
	scale: Pn( Pstep( [ [0,2,4,5,7,0,11], [0,1,3,5,6,8,11] ], 5 ) ),
	db: Pn( Pseg( [-20, -30, -25, -30], 0.4 ) ),
]);
b = Pbind(*[
	degree: Pbrown(0, 6, 1),
	mtranspose: Prand( [\rest, Pseq([0], 5.rand) ], inf),
	dur: 0.2,
	octave: 6
]);
c = Pbind(*[
	degree: [0,2,4],
	mtranspose: Pbrown(0,6,1),
	dur: 0.4,
	db: -35,
]);
d = Pchain( Ppar([b,c]), a )
)
// ------------------------------------------------------
(
var a, b;
a = Pn(Pseq(#[1, 2, 3], 1), 4);    // repeat pattern four times
b = a.asStream;
16.do({ b.next.postln; });
)

// sound example
(
SynthDef(\help_sinegrain,
    { arg out=0, freq=440, sustain=0.05;
        var env;
        env = EnvGen.kr(Env.perc(0.01, sustain, 0.2), doneAction:2);
        Out.ar(out, SinOsc.ar(freq, 0, env))
    }).add;
)

(
var a;
a = Pn(Pshuf([1, 2, 2, 3, 3, 3], 4)).asStream;
{
    loop {
        Synth(\help_sinegrain, [\freq, a.next * 600 + 300]);
        0.2.wait;
    }
}.fork;
)
// ------------------------------------------------------




d.play;

q.play;

p.play;