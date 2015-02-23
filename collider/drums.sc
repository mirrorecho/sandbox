// see here: "http://rumblesan.com/post/53271713518/drum-sounds-in-supercollider-part-1"
// and here: "http://rumblesan.com/post/53271713909/drum-sounds-in-supercollider-part-2"

SynthDesc

( SynthDef('kickdrum', {

    var subosc, subenv, suboutput, clickosc, clickenv, clickoutput;

    subosc = {SinOsc.ar(40,mul:0.3) + SinOsc.ar(50,mul:0.3) + SinOsc.ar(60,mul:0.3)};
    subenv = {Line.ar(1, 0, 0.5, doneAction: 2)};

    clickosc = {LPF.ar(WhiteNoise.ar(0.8),200)};
    clickenv = {Line.ar(1, 0, 0.04)};

    suboutput = (subosc * subenv);
    clickoutput = (clickosc * clickenv);

    Out.ar(0,
        Pan2.ar(suboutput + clickoutput, 0)
    )

}).send(s); )

// ------------------------------------------------
( SynthDef('openhat', {

var hatosc, hatenv, hatnoise, hatoutput;

hatnoise = {LPF.ar(WhiteNoise.ar(1),6000)};

hatosc = {HPF.ar(hatnoise,2000)};
hatenv = {Line.ar(0.4, 0, 0.4)};

hatoutput = (hatosc * hatenv);

Out.ar(0,
Pan2.ar(hatoutput, 0)
)
}).send(s);

// ------------------------------------------------
SynthDef('closedhat', {

var hatosc, hatenv, hatnoise, hatoutput;0

hatnoise = {LPF.ar(WhiteNoise.ar(1),6000)};

hatosc = {HPF.ar(hatnoise,2000)};
hatenv = {Line.ar(0.4, 0, 0.1)};

hatoutput = (hatosc * hatenv);

Out.ar(0,
Pan2.ar(hatoutput, 0)
)
}).send(s); )
// ------------------------------------------------
(
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

Out.ar(0,
Pan2.ar(fulloutput, 0)
)

}).send(s);
)
// ------------------------------------------------
(
SynthDef('clap', {

var claposc, clapenv, clapnoise, clapoutput;

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

Out.ar(0,
Pan2.ar(clapoutput * clapenv, 0)
)

}).send(s);
)
(
e = (
	instrument: 'snaredrum'
);
)

e.play;



l = Synth('clap');
t = Synth('snaredrum');
o = Synth('openhat');
c = Synth('closedhat');
t = Synth('kickdrum');


