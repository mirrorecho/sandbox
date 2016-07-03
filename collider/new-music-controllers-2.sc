// FIRST, EXECUTE ss.sc FILE:
// .......................................................
// SECOND... RUN THIS TWICE... WHY TWICE???????

(
~ss.start(
	~ss.loadCommon(
		{


		}
	)
);
)
// .......................................................
// THIRD RUN THE BUSSES FILE
// .......................................................
// FOURTH RUN SYNTHS FILE:
// .......................................................
// FINALLY... start the synths...
(
b = Synth("ss.bloo");
~p8 = Synth("ss.pops8");
~p12 = Synth("ss.pops12");
~p16 = Synth("ss.pops16");
~r = Synth("ss.ringy");
~mower = Synth("ss.mower");
)

~mower = Synth("ss.mower");

r = Synth("ss.ringy");
r.stop;






// TESTS ---------------------------------------------

// TEST MSG SENDS:
~me.sendMsg('/pineapple/forward', 0);
~me.sendMsg('/pineapple/back', 0);


// TEST control from max
p = { In.kr(~controlBusBackCount).poll }; // to see the
p.stop;
p.play;