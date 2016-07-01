(
~ss.start(
	~ss.loadCommon(
		{

		}
	)
)
)


(
            SynthDef("ss.masterFx", { arg reverbRoom=2, reverbMix=0.9;
                var sig = In.ar(~ss.bus.master,2);
                sig = FreeVerb2.ar(sig[0], sig[1], room:reverbRoom, mix:reverbMix);
                Out.ar(~ss.bus.masterOut, sig);
            }).add;
)

(
~player = Pbind(*[
					instrument:'ss.spacey',
					degree:0,
					legato:0.4,
					dur:1,
				]).play;
)