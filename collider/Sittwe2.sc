
// COPIED FROM TUTORIALS...


(

//this loads into a buffer the default sound that comes with SuperCollider
//.read brings in the whole sound at once
b = Buffer.read(s,"/media/randall/SP PHD U3/Archive/2014-05-03 Handheld Recordings Download/DR0000_0096.wav"); //store handle to Buffer in global variable b

SynthDef(\playbuf,{ arg out=0,bufnum=0, rate=1, trigger=1, startPos=0, loop=1;
	Out.ar(out,
		Pan2.ar(PlayBuf.ar(1,bufnum, BufRateScale.kr(bufnum)*rate, trigger, BufFrames.ir(bufnum)*startPos, loop),0.0)
	)
}).add;
)


//note how even though the soundfile doesn't loop, the Synth is not deallocated when done
//(it has no envelope function). you'll need to cmd+period to kill it
Synth(\playbuf, [\out, 0, \bufnum, b.bufnum]);

 //play at half rate
Synth(\playbuf, [\out, 0, \bufnum, b.bufnum, \rate, 0.5]);