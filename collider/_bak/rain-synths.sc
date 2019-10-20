

(

// NOTE: this has all been implemented in clio-catalogs. Leaving handy in _bak
// for quick reference purposes only
ClioLibrary.catalog ([\func, \rain], {

	~poppy = { arg kwargs;
		var sig;
		sig = Resonz.ar(WhiteNoise.ar(1.98!8), kwargs[\freq], 0.04, 22) +
		Resonz.ar(WhiteNoise.ar(0.6!8), kwargs[\freq] * 2, 0.01, 22);
		sig = sig * kwargs[\amp] * 0.55;
		kwargs[\sig] = kwargs[\sig] + Splay.ar(sig, spread:0.9);
	};

	~ghost = { arg kwargs, slideTime = 0.8;
		var sig, sig2, env;
		var freq = Lag.kr( kwargs[\freq], slideTime);
		sig = Resonz.ar(Crackle.ar(1.98!2), freq, 0.04, 24) +
		Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 12) +
		Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 6) +
		Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 4) +
		Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 2);
		kwargs[\sig] = kwargs[\sig] + Splay.ar(sig, spread:0.9);
	};

	~tap = { arg kwargs;
		var sig;

		sig = Resonz.ar(
			WhiteNoise.ar(70!2) * Decay2.kr( Trig.kr(kwargs[\amp], 0.01), 0.002, 0.1 ),
			kwargs[\freq],
			0.01,
			6
		).distort * 0.3;
		// sig = DelayL.ar(sig, delayRate*4, delayRate, 0.4, sig);
		DetectSilence.ar( sig, doneAction: 2 );
		kwargs[\sig] = kwargs[\sig] + sig;

	};


	~static = { arg kwargs, slideTime = 0.1;
		var sig = 0;
		var freq = Lag.kr(kwargs[\freq], slideTime);
		var amp =  Lag.kr(kwargs[\amp], slideTime);

		sig = sig + Logistic.ar(chaosParam: 3.8!2, freq:freq, init: 0.5, mul: 0.04, add: 0);
		sig = sig + Logistic.ar(chaosParam: 3.9!2, freq:freq*2, init: 0.5, mul: 0.03, add: 0);
		sig = sig + Logistic.ar(chaosParam: 3.99!2, freq:freq*4, init: 0.5, mul: 0.02, add: 0);

		sig = sig + BHiPass.ar(
			Crackle.ar(chaosParam: 2!2, mul: 0.1, add: 0),
			freq, 0.08, 6
		);

		sig = sig + Resonz.ar(
			Crackle.ar(chaosParam: 2!2, mul: 0.1, add: 0),
			freq*6, 0.4, 4
		);

		// TO DO... move to processing funcs?
		sig = sig + Crackle.ar(chaosParam: 2!2, mul: 0.04, add: 0);

		sig = sig + Resonz.ar(WhiteNoise.ar(0.2!2), 300, 0.001, 8); // DO TO: ???

		sig = sig + Resonz.ar(WhiteNoise.ar(0.1!2), 870, 0.001, 6); // DO TO: ???

		sig = sig + Resonz.ar(WhiteNoise.ar(0.04!2), 2250, 0.001, 4); // DO TO: ???

		kwargs[\sig] = kwargs[\sig] + sig;

	};


	// TO DO... research the SOS UGen i.e. pyshical modeling of this more...
	// based on ixibass: see "https://github.com/codehearts/supercollider-music/blob/master/synths/ixibass.sc"
	// TO DO: this is interesting. How to develop? Is it used effectively?
	// ALSO: it doesn't perform very well
	~bass = {arg kwargs, t_trig=1, rq=0.004;

		var signal, signal1, signal2, b1, b2;

		b1 =  [0, 0.01, 0.02, 0.04] + 1.92; // 1.9522665452781; // = 1.98 * 0.989999999 * cos(0.09);
		b2 =  [0, 0.002, 0.004, 0.009] + 0.99 * -1; // -0.998057;
		// t_trig.scope;
		signal = K2A.ar(t_trig);
		// signal.scope;
		signal = SOS.ar(signal, 0.09, 0.0, 0.0, b1, b2);
		signal = RHPF.ar(signal, kwargs[\freq], rq) + RHPF.ar(signal, kwargs[\freq]/2, rq);
		signal = Splay.ar(signal, 0.66);
		signal = Decay2.ar(signal, 0.4, 0.3) * signal; // TO DO ... research why this mul creates this effect...!
		// signal = signal * signal;
		// DetectSilence.ar(signal, 0.01, doneAction:2);
		// signal1 = signal * EnvGen.kr( Env.perc(0.1, 3, curve:-2), gate:gate, doneAction:2);
		// signal2 = signal * EnvGen.kr( Env.perc, gate:gate, doneAction:2);
		// signal = Splay.ar(signal1 * signal2, 0.66);

		kwargs[\sig] = kwargs[\sig] + signal;

	};

	// default values are "U"-ish
	// USEFUL VARIANTS:
	// (
	// 	U: [formant1: 250, formant2: 595, formant_amp:0.04],
	// 	O: [formant1: 360, formant2: 640, formant_amp:0.01],
	// )
	// TO DO: a better implementation of this might be to use klank to add formants to any signal
	~formantTom = { arg kwargs, noiseAmt=0.5, formant1=250, formant2=595, formantAmp=0.04;

		var sig, lpf_freq;

		var formant_count = kwargs[\def_formant_count] ? 3;
		var formant_spread1 = kwargs[\def_formant_spread1] ? 1.04;
		var formant_spread2 = kwargs[\def_formant_spread2] ? 1.02;
		var noise_spread = kwargs[\def_noise_spread] ? 1.1;

		var freq = kwargs[\freq]; // simply to save typing

		var formant_freqs = ExpRand((formant1/formant_spread1)!formant_count, (formant1*formant_spread1)!formant_count)
		++ ExpRand((formant2/formant_spread2)!formant_count, (formant2*formant_spread2)!formant_count);

		var formant_amps = (formantAmp!formant_count)++((formantAmp/2)!formant_count) * kwargs[\amp];

		lpf_freq = noiseAmt * 16;

		sig = (LPF.ar(Resonz.ar(WhiteNoise.ar(1.0!2), ExpRand(freq, freq*noise_spread), noiseAmt/4.0, 20/noiseAmt), freq*8)
		+ LPF.ar(Resonz.ar(WhiteNoise.ar(1.0!2), ExpRand(freq, freq/noise_spread), noiseAmt/4.0, 20/noiseAmt), lpf_freq*8))
		* AmpComp.kr(freq, 90, 0.1)
		+ Splay.ar(Klang.ar(`[formant_freqs, formant_amps, nil]), 0.3);

		kwargs[\sig] = kwargs[\sig] + sig;

	};



	~rainCymbal = ClioSynthDefFactory(*[
		out:Clio.busses[\master],
		channels:2,
		releaseTime:4,
		attackTime:0.09,
		ampScale:0.4,
		curve:-4,

		{ arg name=\rainCymbal, kwargs;

			SynthDef(name, { arg hit=1, amp=1, bufferRate=1, bufferStart=0;

				var whitenoise, pinknoise, noisy, noisy1, bufferSig,
				fixedFreqs, freqs, ringTimes,
				lodriver, locutoffenv, hidriver, hicutoffenv, buffercutoffenv, sig;

				var buffer = \buffer.ir( kwargs[\buffer] );
				var out = \out.ir( kwargs[\out] );
				var attackTime = \attackTime.ir( kwargs[\attackTime] );
				var releaseTime = \releaseTime.ir( kwargs[\releaseTime] );
				var curve = \curve.ir( kwargs[\curve] );

				bufferSig = PlayBuf.ar(kwargs[\channels],
					bufnum:buffer,
					rate:BufRateScale.kr(buffer) * bufferRate,
					startPos:BufSampleRate.kr(buffer) * bufferStart,
					// doneAction:2,
				);


				whitenoise = WhiteNoise.ar(0.04!2);
				pinknoise = PinkNoise.ar(0.2)!2;

				noisy = (bufferSig*0.6) + whitenoise + pinknoise;

				fixedFreqs=
				// [ 222.4, 282.9, 421.4, 447.9, 474.1, 528.4, 556.2, 678.9, 750.2, 793.6, 876.6, 898.4, 932.6, 999.0, 1045.1, 1237.4, 1324.5, 1439.9, 1636.4, 1755.5, 1987.9, 2479.0, 2513.2, 2684.4, 3292.2, 4127.1, 5262.5, 5897.2, 6244.1, 6586.4, 6944.3, 7374.7, 8430.5, 9619.2, 10867.6, 11583.6, 12873.1, 14808.4, 15952.6, 16299.4 ]
				// [ 238.6, 271.1, 284.8, 290.2, 382.1, 382.6, 431.9, 460.4, 501.3, 551.7, 559.0, 578.0, 599.4, 628.0, 827.5, 1135.8, 1146.0, 1183.3, 1254.9, 1280.3, 1333.6, 1371.0, 1380.6, 1445.6, 1483.7, 1688.1, 1840.9, 1986.2, 4529.4, 4723.3, 4750.1, 5792.4, 8562.5, 8732.8, 11068.9, 12662.4, 14645.6, 16732.4, 17327.4, 19065.3 ]
				[ 266.2, 317.2, 328.9, 351.4, 376.4, 462.9, 476.2, 613.3, 634.9, 652.4, 675.7, 752.6, 844.8, 845.9, 861.5, 908.3, 925.6, 1045.6, 1177.7, 1280.6, 1295.7, 1297.6, 1461.8, 1750.6, 2028.1, 2098.4, 2820.4, 2990.7, 3081.5, 3374.5, 3567.0, 4895.6, 5103.7, 7593.6, 8907.3, 9678.0, 10127.0, 11699.5, 13661.2, 14503.7 ]
				;
				freqs = fixedFreqs ++ 40.collect{ExpRand(200, 12000)};

				ringTimes = Rand(0.1!10,1!10) + 10.collect{|i|0.06*i}.reverse;
				ringTimes = (ringTimes!8).flat;

				locutoffenv = EnvGen.ar(Env.perc(0.3, releaseTime)) * 20000 + 10;
				lodriver = LPF.ar(noisy, locutoffenv);

				hicutoffenv = 10001 - (EnvGen.ar(Env.perc(0.1, releaseTime/2)) * 10000);
				hidriver = HPF.ar(pinknoise, hicutoffenv);
				hidriver = hidriver * EnvGen.ar(Env.perc(0.1, releaseTime/2, 0.25));
				// hidriver = [0,0];

				sig  = Klank.ar(`[freqs,0.02!80,ringTimes], lodriver+hidriver);

				buffercutoffenv = EnvGen.ar(Env.perc(0.1, releaseTime+1)) * 20000 + 10;
				noisy1 = LPF.ar(bufferSig, buffercutoffenv) + hidriver + lodriver;

				sig = (sig + (noisy1/3)) * EnvGen.ar(Env.perc(attackTime, releaseTime, curve:curve), doneAction:2);
				sig =  sig + (noisy*(EnvGen.ar(Env.perc(0.001, 0.5)))*hit);
				sig = sig * amp * kwargs[\ampScale];

				Out.ar(out, sig);

			});

	}]);


	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------

	// TO DO... ARE THESE SYNTHS USED...?

	~rainSpacey = ClioSynthDefFactory(*[
		out:Clio.busses[\master],

		{ arg name=\rainSpacey, kwargs;

			SynthDef(name, { arg freq=440, amp=1, gate=1, out = kwargs[\out];
				var sig1, sig2, env1, env2;
				amp = 0.1*amp;
				sig1 = SinOsc.ar(freq:freq, mul:amp/2);
				sig1 = sig1 + SinOsc.ar(freq:freq*2, mul:amp/3);
				env1 = EnvGen.kr(Env.adsr(0.01, 0.1, 0.4, 0.44), gate:gate);
				sig1 = sig1 * env1;

				sig2 = Pulse.ar(freq:freq, mul:amp/8);
				sig2 = RLPF.ar(sig2, LFNoise1.kr(8!2).range(freq*2, 9900), 0.3);
				env2 = EnvGen.kr(Env.adsr(0.1, 0.2, 0.2, 1.2), gate:gate, doneAction:2);


				sig2 = sig2 * env2;

				sig1 = sig1!2 + sig2;

				Out.ar(out, sig1);

			});

	}]);

	// ---------------------------------------------------------------------------------------------

	~rainClap = ClioSynthDefFactory(*[
		out:Clio.busses[\master],

		{ arg name=\rainClap, kwargs;

			SynthDef(name, {

				var claposc, clapenv, clapnoise, clapoutput, out = kwargs[\out];

				clapnoise = {BPF.ar(LPF.ar(WhiteNoise.ar(1),12000),1500)};
				clapenv = {Line.ar(0.6, 0, 0.2, doneAction: 2)};

				clapoutput = {Mix.arFill(5,
					{arg i;
						EnvGen.ar(
							Env.new(
								[0,0,1,0],
								[0.02 * i,0,0.06]
							)
						) * clapnoise * 0.5
					}
				)};

				Out.ar(out,
					Pan2.ar(clapoutput * clapenv, 0)
				);
			});

	}]);



	// ---------------------------------------------------------------------------------------------

	/*	// A Piano note SynthDef as a combination of its harmonics
	SynthDef("rain.piano",{ arg freq = 261.63, amp = 1, gate = 1, out=~ss.bus.master;
	var ampls = [3.7, 5.4, 1.2, 1.1, 0.95, 0.6, 0.5, 0.65, 0, 0.1, 0.2];
	var freqs = Array.fill(ampls.size, { |i| freq * (i + 1) });
	var waves = Array.fill(ampls.size, { |i| SinOsc.ar(freqs.at(i),mul: ampls.at(i))});
	var mixedwaves = Mix.ar(waves).range(amp * -1, amp) * 0.1;
	var env = Env.perc(0.09, 2,curve: -12);
	var final = mixedwaves * EnvGen.ar(env, gate, doneAction: 2);
	Out.ar(out, final!2);
	}).add;*/



}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.
	// p.patterns.putFromEnvir(envir);

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)

