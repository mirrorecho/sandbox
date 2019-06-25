

// SEE: http://www.mcld.co.uk/cymbalsynthesis/ !!!!

x = { Ringz.ar(PinkNoise.ar(0.1), {exprand(300, 20000)}.dup(100)).mean }.play;
// slightly more condensed:
x = { Ringz.ar(PinkNoise.ar(0.1), Array.exprand(100,300,20000)).mean }.play;
x.free;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

(
x = {
	var driver, cutoffenv, freqs, res;
	cutoffenv = EnvGen.ar(Env.perc(0.25, 5)) * 20000 + 10;
	driver = LPF.ar(WhiteNoise.ar(0.1), cutoffenv);
	freqs  = Array.exprand(100,300,20000);
	res    = Ringz.ar(driver, freqs).mean!2;
}.play;
)
(// performs much better:
x = {
	var driver, cutoffenv, freqs, res;
	cutoffenv = EnvGen.ar(Env.perc(0.25, 5)) * 20000 + 10;
	driver = LPF.ar(WhiteNoise.ar(0.1), cutoffenv);
	freqs  = Array.exprand(100,300,20000);
	res    = Klank.ar(`[freqs,0.01!100,nil], driver)!2;
}.play;
)
x.free;

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

(

x = {

	var lodriver, locutoffenv, hidriver, hicutoffenv, freqs, res;
	locutoffenv = EnvGen.ar(Env.perc(0.25, 5)) * 20000 + 10;
	lodriver = LPF.ar(WhiteNoise.ar(0.1), locutoffenv);

	hicutoffenv = 10001 - (EnvGen.ar(Env.perc(0.1, 3)) * 10000);
	hidriver = HPF.ar(WhiteNoise.ar(0.1), hicutoffenv);
	hidriver = hidriver * EnvGen.ar(Env.perc(0.1, 2, 0.25));

	freqs  = Array.exprand(100,300,20000);
	res    = Ringz.ar(lodriver + hidriver, freqs).mean!2;
}.play;

)

// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

(

x = {
	var lodriver, locutoffenv, hidriver, hicutoffenv, freqs, res;
	locutoffenv = EnvGen.ar(Env.perc(0.5, 5)) * 20000 + 10;
	lodriver = LPF.ar(WhiteNoise.ar(0.1), locutoffenv);
	hicutoffenv = 10001 - (EnvGen.ar(Env.perc(0.1, 3)) * 10000);
	hidriver = HPF.ar(WhiteNoise.ar(0.1), hicutoffenv);
	hidriver = hidriver * EnvGen.ar(Env.perc(0.1, 2, 0.25));
	freqs  = {exprand(300, 20000)}.dup(100);
	res    = Ringz.ar(lodriver + hidriver, freqs).mean!2;
	(res + (lodriver * 2))!2;
}.play;

)
// ------------------------------------------------------------------------
// ------------------------------------------------------------------------

(

SynthDef("ss.cymbal", { arg pan=0, attackTime=0.01, releaseTime=4, hit;
	var lodriver, locutoffenv, hidriver, hicutoffenv, freqs, sig, whitenoise;

	whitenoise = WhiteNoise.ar(0.1);

	locutoffenv = EnvGen.ar(Env.perc(0.5, releaseTime+1)) * 20000 + 10;
	lodriver = LPF.ar(whitenoise, locutoffenv);

	hicutoffenv = 10001 - (EnvGen.ar(Env.perc(0.1, releaseTime)) * 10000);
	hidriver = HPF.ar(whitenoise, hicutoffenv);
	hidriver = hidriver * EnvGen.ar(Env.perc(0.1, releaseTime/2, 0.25));

	freqs  = Array.exprand(120,300,20000);
	sig    = Ringz.ar(lodriver + hidriver, freqs).mean;

	sig = (sig + lodriver + whitenoise);

	sig = Pan2.ar(sig, pan, EnvGen.ar(Env.perc(attackTime, releaseTime), doneAction:2));

	Out.ar(~ss.bus.master, sig);

}).add;


SynthDef("ss.cymbal2", { arg pan=0, attackTime=0.01, releaseTime=4, hit;
	var lodriver, locutoffenv, hidriver, hicutoffenv, freqs, sig,
	whitenoise, pinknoise, curves, noisy1, noisy2, numCurves,
	numFreqs, fixedFreqs, rate=1, buffer=~ss.buf['japan-cicadas']['0159-insects-in-kyoto'],
	start=0, bufsig;

	bufsig = PlayBuf.ar(2,
			bufnum:buffer,
			rate:BufRateScale.kr(buffer)*rate,
			startPos:BufSampleRate.kr(buffer) * start,
		// doneAction:2,
		);
	bufsig=[0,0];

	whitenoise = WhiteNoise.ar(0.05) + bufsig[0];
	pinknoise = PinkNoise.ar(0.4) + bufsig[1];

	fixedFreqs = [
		[ 465.0, 1518.0, 16477.0, 2399.0, 10021.0, 1617.0, 3716.0, 3478.0, 15202.0, 1983.0 ],
		[ 2314.0, 5460.0, 465.0, 6751.0, 1518.0, 354.0, 1775.0, 489.0, 3297.0, 709.0 ],
		[ 441.0, 462.0, 1465.0, 2159.0, 8355.0, 1174.0, 1722.0, 6867.0, 3132.0, 714.0 ],
		[ 18368.0, 18685.0, 3059.0, 17376.0, 13092.0, 345.0, 4324.0, 342.0, 9999.0, 2166.0 ],
	];
	freqs = fixedFreqs.collect{|a|
		a ++ 10.collect{ExpRand(300, 20000);};
	};

	curves = {Rand(-4.0,4.0)}!freqs.size;

	locutoffenv = curves.collect{|c|
		EnvGen.ar(Env.perc(0.5, releaseTime+1, curve:c) ) * 20000 + 10};
	lodriver = LPF.ar(whitenoise, locutoffenv);

	hicutoffenv = 10001 - (EnvGen.ar(Env.perc(0.1, releaseTime)) * 10000);
	hidriver = HPF.ar(pinknoise, hicutoffenv);
	hidriver = hidriver * EnvGen.ar(Env.perc(0.1, releaseTime/2, 0.25));

	sig  = ({|i| Ringz.ar(lodriver[i] + hidriver, freqs[i]).mean;}!curves.size).mean;

	noisy1 = (hidriver/2) + lodriver.mean + whitenoise;
	noisy2 = noisy1 + pinknoise;

	sig = (sig + noisy1 + (EnvGen.ar(Env.perc(0.001, 0.5))*noisy2) );

	sig = Pan2.ar(sig, pan, EnvGen.ar(Env.perc(attackTime, releaseTime), doneAction:2));

	Out.ar(~ss.bus.master, sig);

}).add;


SynthDef("ss.cymbal3", { arg attackTime=0.09, releaseTime=4, hit=1, amp=1, curve= -4.0,
	buffer=~ss.buf['japan-cicadas']['0159-insects-in-kyoto'], bufferRate=1, bufferStart=0,
	out=~ss.bus.master;

	var whitenoise, pinknoise, noisy, noisy1, bufferSig,
	fixedFreqs, freqs, ringTimes,
	lodriver, locutoffenv, hidriver, hicutoffenv, buffercutoffenv, sig;

	bufferSig = PlayBuf.ar(2,
		bufnum:buffer,
		rate:BufRateScale.kr(buffer) * bufferRate,
		startPos:BufSampleRate.kr(buffer) * bufferStart,
		// doneAction:2,
	);


	whitenoise = WhiteNoise.ar(0.04!2);
	pinknoise = PinkNoise.ar(0.2)!2;

	noisy = (bufferSig*0.6) + whitenoise + pinknoise;

	fixedFreqs=
	[ 222.4, 282.9, 421.4, 447.9, 474.1, 528.4, 556.2, 678.9, 750.2, 793.6, 876.6, 898.4, 932.6, 999.0, 1045.1, 1237.4, 1324.5, 1439.9, 1636.4, 1755.5, 1987.9, 2479.0, 2513.2, 2684.4, 3292.2, 4127.1, 5262.5, 5897.2, 6244.1, 6586.4, 6944.3, 7374.7, 8430.5, 9619.2, 10867.6, 11583.6, 12873.1, 14808.4, 15952.6, 16299.4 ]
	// [ 238.6, 271.1, 284.8, 290.2, 382.1, 382.6, 431.9, 460.4, 501.3, 551.7, 559.0, 578.0, 599.4, 628.0, 827.5, 1135.8, 1146.0, 1183.3, 1254.9, 1280.3, 1333.6, 1371.0, 1380.6, 1445.6, 1483.7, 1688.1, 1840.9, 1986.2, 4529.4, 4723.3, 4750.1, 5792.4, 8562.5, 8732.8, 11068.9, 12662.4, 14645.6, 16732.4, 17327.4, 19065.3 ]
	// [ 266.2, 317.2, 328.9, 351.4, 376.4, 462.9, 476.2, 613.3, 634.9, 652.4, 675.7, 752.6, 844.8, 845.9, 861.5, 908.3, 925.6, 1045.6, 1177.7, 1280.6, 1295.7, 1297.6, 1461.8, 1750.6, 2028.1, 2098.4, 2820.4, 2990.7, 3081.5, 3374.5, 3567.0, 4895.6, 5103.7, 7593.6, 8907.3, 9678.0, 10127.0, 11699.5, 13661.2, 14503.7 ]
	;
	freqs = fixedFreqs ++ 40.collect{ExpRand(290, 12000)};

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
	sig = sig*amp;

	Out.ar(out, sig);

}).add;


)
~ss.bus.master;

Array.exprand(40,220,18000).round(0.1).sort;

Synth("ss.cymbal", [releaseTime:2]);
Synth("ss.cymbal2", [releaseTime:4]);
Synth("ss.cymbal3", [releaseTime:1, hit:0]);
Synth("ss.cymbal3", [releaseTime:1, hit:1]);
Synth("ss.cymbal3", [releaseTime:4, curve:0]);
(
Synth("ss.cymbal3", [releaseTime:2, buffer:~ss.buf['japan-cicadas']['0185-insects-water-kyoto'],
	bufferStart:4.0.rand, curve:-22, hit:1.0.rand]);
)
2.0.rand;

~ss.buf['japan-cicadas']['0185-insects-water-kyoto'].play;

a=[1,2,3,4];
(a!4).flat;

[1,4,3,5].collect{|c| c+1;};

(
	~fixedFreqs = [
		[ 465.0, 1518.0, 16477.0, 2399.0, 10021.0, 1617.0, 3716.0, 3478.0, 15202.0, 1983.0 ],
		[ 2314.0, 5460.0, 465.0, 6751.0, 1518.0, 354.0, 1775.0, 489.0, 3297.0, 709.0 ],
		[ 13191.0, 17941.0, 507.0, 17228.0, 907.0, 13266.0, 6137.0, 784.0, 7022.0, 886.0 ],
		[ 19415.0, 302.0, 12791.0, 369.0, 2768.0, 512.0, 302.0, 341.0, 5392.0, 11630.0 ],
	];

b = ~fixedFreqs.collect{|a|
	a ++ Array.exprand(10,300,20000).round;
};

)

10.collect{|i|0.06*i}.reverse;


~fixedFreqs.size;

b[2][10..];

14.class;
14.0.asInteger;

(4/2).asInteger;



l = 1!14;

"A"!l.size;

[10,20,30,40,50,60][..(4/3).asInteger];
[10,20,30,40,50,60][2..];

Float

Array

Rand



(

x = {
	var lodriver, locutoffenv, hidriver, hicutoffenv, freqs, res, thwack;
	thwack = EnvGen.ar(Env.perc(0.001,0.001,1));
	freqs  = {exprand(300, 20000)}.dup(100);
	res    = Ringz.ar(thwack, freqs).mean;
	(thwack).dup;
}.play;

)

[1,2]++[3,4];



Array.linrand(6,-8.0,8.0);

Env.perc(0.5, 2, [-4,0,4])[2];

11.rand+11;

x.free;

x.free; // To stop



Array.exprand(100,10,100);
{exprand(10,100)}.dup(100);
exprand(10,100).dup(100);

[0,1,2,3,4].mean!2;