// GO!!!!: .......................................................
(

fork {
	s.reboot;
	3.wait;
	~localPath = "".resolveRelative;
	(~localPath ++ "../../superstudio/ss.sc").loadPaths;
	~ss.start( ~ss.loadCommon({}) );
	1.wait;
	~ss.start( ~ss.loadCommon({}) );
	1.wait;
	(~localPath ++ "new-music-controllers-busses-2.sc").loadPaths;
	2.wait;
	(~localPath ++ "new-music-controllers-synths-2.sc").loadPaths;
	3.wait;
	~pops = [];
	{|i|
		~pops.add(Synth("ss.pops." ++ (i+1) ));
	}!24;
	~ringy = Synth("ss.ringy");
	~mower = Synth("ss.mower");
	~bloo = Synth("ss.bloo");
	~cicadas = Synth("ss.cicadas");
};
)

// {SinOsc.ar}.play;


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