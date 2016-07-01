// SETUP
(
~ss.loadCommon();
)

// ------------------------------------------------
( SynthDef('openhat', {

var hatosc, hatenv, hatnoise, hatoutput;

hatnoise = {LPF.ar(WhiteNoise.ar(1),6000)};

hatosc = {HPF.ar(hatnoise,2000)};
hatenv = {Line.ar(0.4, 0, 0.4)};

hatoutput = (hatosc * hatenv);

Out.ar(~ss.bus.master,
Pan2.ar(hatoutput, 0)
)
}).add;


// ------------------------------------------------
SynthDef('closedhat', {

var hatosc, hatenv, hatnoise, hatoutput;

hatnoise = {LPF.ar(WhiteNoise.ar(1),6000)};

hatosc = {HPF.ar(hatnoise,2000)};
hatenv = {Line.ar(0.4, 0, 0.1)};

hatoutput = (hatosc * hatenv);

Out.ar(~ss.bus.master,
Pan2.ar(hatoutput, 0);
)
}).add;


SynthDef('snaredrum', {

var drumosc, filterenv, volenv, drumoutput, snaposc, snapenv, fulloutput;

drumosc = {Pulse.ar(200,mul:0.2) + Pulse.ar(390,width:0.3,mul:0.3) + Pulse.ar(590,width:0.7,mul:0.3)};
filterenv = {Line.ar(0.3, 0, 0.2, doneAction: 0)};
volenv = {Line.ar(0.3, 0, 0.6, doneAction: 2)};
drumoutput = {LPF.ar(drumosc,(filterenv *1000) + 30,mul:0.6)};

snaposc = {BPF.ar(HPF.ar(WhiteNoise.ar(1),500),1500)};
snapenv = {Line.ar(0.3, 0, 0.2, doneAction: 0)};

fulloutput = (drumoutput * volenv) + (snaposc * snapenv);
//fulloutput = (drumoutput * volenv);

Out.ar(~ss.bus.master,
Pan2.ar(fulloutput, 0)
)

}).add;


SynthDef(\weirdo, {
	arg noiseHz=12, hiFreq=440, loFreq=880, weirdCount=4, gate=1;
	var amp, freq, sig, sig2, env, weirdCount2=8;
	freq = LFNoise2.kr(noiseHz!weirdCount2).exprange(loFreq, hiFreq);
	amp = LFNoise1.kr(noiseHz!weirdCount2).exprange(0.01, 0.6);
	amp = amp / weirdCount;
	sig = SinOsc.ar(freq) * amp;
	sig2 = Splay.ar(sig, spread:0.6);
	// sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.1);
	env = EnvGen.kr(Env.asr, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(~ss.bus.master, sig2);
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
	Out.ar(~ss.bus.master, sum);
}).add;




)





// THE SCORE
// -----------------------------------------------
(
~player = Pbind(*[
	instrument:'snaredrum',
	degree:0,
	legato:0.4,
	dur:1,
]).play.stop;
)

~player.play;
~player.resume;
~player.stop;
