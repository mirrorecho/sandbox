p = ProxySpace.push;

~out.play;
~x = 0.2; ~a = 1.1; ~c = 0.13;
~x = (~a * ~x) + ~c % 1.0;

~l = { Line.kr(120,12000,10) };
~l = { Line.kr(12000,120,10) };

~out = { Pan2.ar(SinOsc.ar(~x * ~l + 200) * 0.1, ~x) };

p.clear(4).pop;



p = ProxySpace.push;
(
~a = {
	var a;
	a = Lag.ar(LFClipNoise.ar(2 ! 2, 0.5, 0.5), 0.2);
	BPF.ar(~c.ar * 5, a * 3000 + 1000, 0.1);
}
);

~c = { Dust.ar(20 ! 2) };
~d = { Decay2.ar(~c.ar, 0.01, 0.02, SinOsc.ar(11300)) };
~b = ~a + ~d;

~b.play;

// LazyEnvir