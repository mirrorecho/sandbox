// ZERO, REBOOT: .......................................................
(
s.reboot;
)
// FIRST, EXECUTE ss.sc FILE:
// .......................................................
(
~ss.localPath = "".resolveRelative;
(~ss.localPath ++ "../../superstudio/ss.sc").loadPaths;
)
// SECOND... RUN THIS TWICE... WHY TWICE???????
(
~ss.start( ~ss.loadCommon({}) );
)
// .......................................................
// THIRD RUN THE BUSSES
(
~ss.localPath = "".resolveRelative;
(~ss.localPath ++ "new-music-controllers-busses-2.sc").loadPaths;
)
// .......................................................
// FOURTH RUN SYNTHS
(
(~ss.localPath ++ "new-music-controllers-synths-2.sc").loadPaths;
)
// .......................................................
// FINALLY... start the synths...
(
~pops = [];
{|i|
	~pops.add(Synth("ss.pops." ++ (i+1) ));
}!24;
// ~ringy = Synth("ss.ringy");
~mower = Synth("ss.mower");
~bloo = Synth("ss.bloo");
// ~cicadas = Synth("ss.cicadas");
)

{SinOsc.ar}.play;


// TO STOP THE SYNTHS:
(
{|i|
	~pops.[0].stop;
}!16;
~mower.stop;
~ringy.stop;
~cicadas.stop;
)






// TESTS ---------------------------------------------

// TEST MSG SENDS:
~me.sendMsg('/pineapple/forward', 0);
~me.sendMsg('/pineapple/back', 0);


// TEST control from max
p = { In.kr(~controlBusBackCount).poll }; // to see the
p.stop;
p.play;