

s.boot

(

SynthDef(\weirdo, {
	arg noiseHz=12, hiFreq=440, loFreq=880, weirdCount=4, gate=1;
	var amp, freq, sig, sig2, env, weirdCount2=8;
	freq = LFNoise2.kr(noiseHz!weirdCount2).exprange(loFreq, hiFreq);
	amp = LFNoise1.kr(noiseHz!weirdCount2).exprange(0.01, 0.6);
	amp = amp / weirdCount;
	sig = SinOsc.ar(freq) * amp;
	sig2 = Splay.ar(sig, spread:0.6);
	sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.1);
	env = EnvGen.kr(Env.asr, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(0, sig2);
}).add;

)


(
SynthDef(\cooleo, {
	arg freq=440;
	var temp, env, sum=0, numCools=44;
	numCools.do{
		temp = VarSaw.ar(
			freq * {Rand(0.99, 1.02)}!2,
			iphase: {Rand(0.0, 1.0)}!2,
			width: {ExpRand(0.01, 1)}!2,
			mul: 1/numCools);
		sum = sum + temp;
	};
	env = EnvGen.kr(Env.perc, doneAction:2);
	Out.ar(0, sum * env);
}).add;

SynthDef(\bloo, {
	arg freq = 440;
	var temp, sum=0, numBloo=16;
	numBloo.do{
		arg count;
		temp = SinOsc.ar(
			freq
			* LFNoise2.kr({Rand(0.2, 4)}!2).exprange(0.99, 1.01)
			* (count + 1),
			mul: 1 / ((count+1)/2)
		);
		temp = temp * LFNoise2.kr({Rand(0.5, 4)}!2).exprange(0.01, 1);
		sum = sum + temp;
	};
	sum = (sum / numBloo) * 0.8;
	sum = FreeVerb2.ar(sum[0], sum[1]);
	Out.ar(0, sum);
}).add;


)


(
['yo', 'mama'].do({
	arg blah, count;
	[count, blah].postln;
})
)


(
s.plotTree;
s.meter;
)

(
y = Synth(\bloo, [\freq, 80]);
)



(
var loFreq = 440;
var hiFreq = 440 * 2;

x = Synth.new(\weirdo, [
	\noiseHz, 4,
	\weirdCont, 4,
	\loFreq, loFreq,
	\hiFreq, hiFreq]);
)

(
x.set(\gate, 0);
y.set(\gate, 0);
)

x.free;