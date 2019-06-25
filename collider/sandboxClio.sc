
// RUN FIRST
(
Clio.go {
	// ~sandboxSounds = ClioSoundLibrary("/Users/rwest/Echo/Sounds/Library/");
	// c = ClioProject("sandbox", "".resolveRelative);
	~sandboxSounds = ClioSoundLibrary("/Users/rwest/Echo/Sounds/Library/");
	~cSound = ~sandboxSounds.atKey([\piano, \A1]);

	"HURRAY!".postln;
};
// s.reboot;
)

// RUN SECOND???
// (
// c.sounds.load
// )



(

)


~cSound.buffer;
~cSound.path;
Clio.openAll;
~sandboxSounds.postTree;


a = [aa:44, numChannels:2];
c = [aa:55, cc:66, ff:{arg yoyo; yoyo.postln;}];

b = (a.asDict ++ c.asDict).asPairs.asDict;

b[\ff].value(22);
b[\oo] = "CHACHA";
b[\oo];


b.know = true;

b;
b.play;

IdentityDictionary


a = [a:10, b:11, c:12, d:nil, e:44];
a.asDict;


(
f = ClioSamplerFactory(*[
	out:Clio.busses[\master],
	{ arg name, kwargs;

		// "------------------------------------------------".postln;
		// kwargs.numChannels.postln;

		SynthDef(name, {
			arg amp=1.0, start=0, freq=440, out=kwargs[\out];
			var buffer, bufferFreq, rate, sig;


			#buffer, bufferFreq = kwargs[\getSample].value(freq);
			rate = freq / bufferFreq;

			// sig = SinOsc.ar(mul:0.1);

			sig = PlayBuf.ar(
				numChannels: kwargs[\numChannels],
				bufnum:buffer,
				rate:BufRateScale.kr(buffer)*rate,
				startPos:BufSampleRate.ir(buffer) * start,
				doneAction:2,
			);

			sig = sig * amp;

			Out.ar(out, sig);

		});
	},
]);
)

(
~pianoYa = f.mimic(
	*[
		soundLibrary:~sandboxSounds,
		keysAndFreqs:[
			[['piano', 'A2'], 110],
			[['piano', 'A3'], 220],
			[['piano', 'A4'], 440],
			[['piano', 'A5'], 880],
			[['piano', 'A6'], 1760]
		],
		numChannels:2,
	]
);
)

~pianoYa.args.postln;

~pianoYa.args.asDict;



~pianoYa.add(\pianoYa2);

~pianoYa.args.asDict[\numChannels];
~pianoYa.args.asDict.postln;


Synth(\pianoYa2, [freq:110]);


SoundFile.collect("/Users/rwest/Echo/Sounds/Library/stringy/*");

(
p.patterns.putFactories(
	ClioP,
	*[
		melody: [ Pbind,
			note:Pseq([1,2,3,4])
		],

	];
)
)

~stuff = ClioLibrary("".resolveRelative ++ "sandboxClioLoad.sc");


~stuff.putFromCatalog([], "".resolveRelative ++ "sandboxClioLoad.sc");

~stuff[\yoMama1].value;
~stuff[\yoMama2].value;


f = Environment.make;
f.use( {("".resolveRelative ++ "sandboxClioLoad.sc").load;});


(
f = ClioP(Pbind,*[
	dur: Pseq([1,2,3,1]),
	note: 4,
]);
)

[dur:1, note:2];

f.play;
g = f.mimic(\note, ClioP(Pseq, [0,2], inf));

f.args;
g.args;
g.play;

().asPairs;

g = f.copy;
h = f.deepCopy;

f.args[1].list[3];
f.args[1].list[3] === g.args[1].list[3];
f.args[1].list[3] === h.args[1].list[3];

f.makeType;

b = f.make;
c = f.make;
b == c;

p.path;

a = [1,2,3,Pbind()];
b = a.deepCopy;

b[3]===a[3];

Array.deepCopy


Clio.openAll;
Library;
~sandboxSounds.postTree;
~soundFileA1 = ~sandboxSounds[\piano, \A1];


p = ClioProjectLibrary.new;
q = ClioProjectLibrary.new;

p.go;

p.postTree;


(

l = ClioLibrary.new;
l.putFromFile(\loadTest, "".resolveRelative ++ "sandboxClioLoad.sc");

)

// REMEMBER .range and .exprange
topEnvironment.keys;


l[\yoMama2];

e[\yoMama2];
e.collect { arg ...args; args.postln; };

keyboard


e;
(
e.collect { arg ...args;
	args[1].class.postln;
};
)


~sandboxSounds.postTree;


~stuff.value;


b = ClioBufferLibrary();
b.makeBuffer(\sec2, 2, 2,);


b.removeFree(\sec2);
b[\sec2];

b.makeBuffer([\sec2a, \sec2a2], 2.2, 2);

b.postTree;

b[\sec2a, \sec2a2];
b.at([\sec2a, \sec2a2]);


Clio.busses.makeBus([\yo, \ya]);

Clio.busses.removeFree(\yo);
Clio.busses.free;

Clio.busses.postTree;


Clio.synths.makeSynth(\commonPop, args:[\amp, 0.1]);


(
f = ;

)

l.postTree;
l.play([\piano, \A2]);
o = l[\piano, \A2].cue;
o.play;

l[\piano, \A1];

(
b = l.toBufferLibrary(\shamisen);
b = l.toBufferLibrary([\clangy], b);
)

b;
b.postTree;

(
l.leafDoFrom(\piano.asArray, {arg ...args;
	args.postln;
});
)

nil.asArray;

(
l.asArray.do { arg item;
	item.postln;
}
)

Library;
MultiLevelIdentityDictionary;

t = TreeView();
t.insertItem(0, ["YO"]);
t.focus;
t.front;


l.asArray;

l.put(\shamisen, \foo, \fanny, "MOO");

l[\clangy, 'garlic-peel-1'].path;


l.put(*['clangy', 'faa-1', 1]);
l.put(*['clangy', 'faa-2', 2]);

l['0191-insects-temple'];

o = SoundFile.collect("/Users/rwest/Echo/Sounds/Library/clangy/*")[0];
o.asBuffer;

c = SoundFile.collectIntoBuffers(o.path)[0];


l = Library.new;
l.put(*[\yo, \ha, SoundFile.collect("/Users/rwest/Echo/Sounds/Library/clangy/*")[0]]);
l.[\yo, \ha]


SoundFile.collect("/Users/rwest/Echo/Sounds/Library/clangy/*")[0]

SoundFile.collect("/Users/rwest/Echo/Sounds/Library/clangy/*")[0].path.basename.splitext[0];


PathName("/Users/rwest/Echo/Sounds/Library/").pathOnly;

Clio.filenameSymbol.asString.resourceDir;

Clio.filenameSymbol;

"".resolveRelative;

Clio.inspect;

(
PathName(f).folders.do {arg folder;
	folder.folderName.postln;
};
)


(
SynthDef( \commonPop, {
	arg freq=440, gate=1, amp=1.0, slideTime = 1.0, out= Clio.busses[\master];
	var sig, sig2, env;
	freq = Lag.kr(freq, slideTime);
	sig = Resonz.ar(WhiteNoise.ar(1.98!2), freq, 0.04, 22) +
	Resonz.ar(WhiteNoise.ar(0.6!2), freq * 2, 0.01, 22);
	sig = sig * amp * 8;
	sig2 = Splay.ar(sig, spread:0.9);
	sig2 = FreeVerb2.ar(sig2[0], sig2[1], mix:0.4);
	env = EnvGen.kr(Env.perc, gate:gate, doneAction:2);
	sig2 = sig2 * env;
	Out.ar(out, sig2);
}).add;
)



Clio.busses.makeControlBus(\yo2);


Clio.busses.at("yo".asSymbol);


c.removeBus("myBus");

c.postTree;

c[\myBus];


c.makeBus;

Clio.server;

Clio.testMe;

c = ClioProject.new;

c.showArgs1("C", "A", "B");
c.showArgs1(cc:"C", aa:"A", bb:"B");


(
c.showArgs2(
	[\a->"C",
	\b->"A",
		\c->"B"]
);
)

e = ().add([\a->"C", \b->"A", \c->"B"]);

c.showArgs2((cc:"C", aa:"A", bb:"B").asPairs);

Dictionary


l = Library.new;
l.put(\multi, \level, \addressing, \system, "i'm the thing you are putting in here");
l.at(\multi, \level, \addressing, \system).postln;

l.at(\yoMama, \fa);

m = Library.new;
l[\multi].put(\level2,(a:1));

l.postTree;
l[\multi, \level2];

Catalog

(
p = SsProject.new {
	~ya = "FOFO";
};
)

// CLASSES I'LL WANT


// p.ya;
// Pbindf
//
// a = ();
// a.copy;
//
//
// // STUFF IN SS ARE CLASSES
//
// Clio.go {}; // startup everything ...
//
// ~rain = ClioProject("".resolveRelative);
//
//
//
//
// Clio.patternFactories;
// Clio.factories;
// Clio.libraries;
// Clio.markets;
//
//
// ClioPatternFactory
// ClioPbind
// ClioPmono
//
//
// ClioPatternLibrary
//
//
//
// go;
//




// LOOK MORE INTO linkDoc

e = ();
f = {};

e->f;
g = f->e;

(
~ss = Environment.make;
~ss.know = true;


)

(
~ss = Environment.make;
~ss.know = true;
~ss.inherit = {arg self ...args;
	args.
};
)

e.func {"YO";}, ;


(
~ss = Environment.make;
~ss.know = true;

~ss.inherit = { | self, func |
	var envir = self ++ Environment.make;
	envir.use(func);
	envir;
};

~ss.makeClass = { | self, func |
	var myClass = self.inherit(func);

	var makeInstance = { | instanceFunc |
		var envir = myClass ++ Environment.make;
		envir.use(instanceFunc);
		envir.removeAt(\inherit);
		envir.removeAt(\makeClass);
		envir;
	};
	makeInstance;
};

)


e = (); f = ();

g = e <> {1;};



~ss.patterns = ~ss.makeClass;

~ss.patterns;


a = ~ss.patterns {~yo="FOO";};
a;



~ss.stuff = { arg ... args;
	var self = args[0];
	var myStuff = self.makeEnvir;
	myStuff;
};

)


~ss.stuff({"a".postln;});



(
~ss.patterns = ~ss.namespace {
	~makeWork = {|self, yo| yo.postln;};
};
)

~ss.patterns;
~ss.patterns.makeWork("HA");


~ss.makeSs {
	~fanny = \pak;
};
)


~ss;

a = (a:"JD");

Document.current.path;
Namespace

(
v = Environment.make;
v.know = true;
v.yaya = "FOFO";
v.funcy = {arg...args; args.postln;};
// v.linkDoc(Document.current);
)

v @@ 2;

v.know = false;

v.funcy.value;
v.funcy.value("ha","foo");
v.funcy(mama:"ha",yo:"foo");
v;

f.value(mama:"ha",yo:"foo");

(
v.use {
	~yaya.postln;
	~yoyo = \mamamama
};
)


v;

g = Environment.make;
g.know = true;
g.blob = "poop";


f = g ++ v;
f;


f = {|yo, mama| (yo + mama).postln;};

(
~yaya;
)



e = (yo:\mama);
e.keys;

e.play;
v.play;

w = v ++ e;
w.know = true;

w.ha = "fa";
w.keys;
w.play;

(
e.use {
	~yo.postln;
};
)