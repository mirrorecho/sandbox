Buffer.freeAll;  // how to do this better...?


// define all the synths in this block...
(

// ----------------------------------------------------------------------------------------------------------
// boat motor sound that continues indefinitely:

var boatFileName = "/media/randall/SP PHD U3/Archive/2014-05-03 Handheld Recordings Download/DR0000_0096.wav";
var boatBufSampleRate = SoundFile(boatFileName).sampleRate;
var boatStartOn = 6;
var boatLength = 2;

// NOTE... butter off to use cue (or some other method for SoundFile instead of re-reading the sound file)
var boatBuf = Buffer.read(s, boatFileName, boatBufSampleRate*boatStartOn,  boatBufSampleRate*boatLength);



// ----------------------------------------------------------------------------------------------------------
// a bell from Shwedagon:
var bell122_fileName = "/media/randall/SP PHD U3/Archive/2014-05-03 Handheld Recordings Download/DR0000_0122.wav";
var bell122_bufSampleRate = SoundFile(bell122_fileName).sampleRate;
var bell122_startOn = 16;
var bell122_length = 20;

// NOTE... butter off to use cue (or some other method for SoundFile instead of re-reading the sound file)
var bell122_buf = Buffer.read(s, bell122_fileName, bell122_bufSampleRate*bell122_startOn, bell122_bufSampleRate*bell122_length);

SynthDef(\boat,{ arg out=0, rate=1, trigger=1, startPos=0, loop=1, fadein=2, sustain=4, fadeout=2;
	Out.ar(out,
		PlayBuf.ar( 2,
			boatBuf.bufnum,
			BufRateScale.kr(boatBuf.bufnum)*rate,
			trigger,
			BufFrames.ir(boatBuf.bufnum)*startPos,
			loop, doneAction:2)
		* EnvGen.ar(Env.linen(fadein, sustain, fadeout),doneAction:2) * 0.6
		,0.0)
}).add;


SynthDef(\bell122,{ arg out=0,rate=1, trigger=1, startPos=0;
	Out.ar(out,
		PlayBuf.ar( 2,
			bell122_buf.bufnum,
			BufRateScale.kr(bell122_buf.bufnum)*rate,
			trigger,
			BufFrames.ir(bell122_buf.bufnum)*startPos,
			doneAction:2)
		,0.0)
}).add;

)



//s.sync; // this waits for everything to load before continuing...

(

Routine.run({
	var repeats = 4, initialWait = 4, repeatWait = 6, bellWait = 0.4;

	Synth(\boat, [
		\fadein, initialWait,
		\sustain, (bellWait + repeatWait) * repeats,
		\fadeout, initialWait * 2
	]);

	initialWait.wait;

	repeats.do {
		//rrand(low, hi) gets a uniform random number in that range
		Synth(\bell122,[\rate, rrand(1.2, 1.6)]);
		bellWait.wait;
		Synth(\bell122,[\rate, rrand(1.2, 1.6)]);
		repeatWait.wait;
	};


});

)



