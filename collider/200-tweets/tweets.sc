
// --------------------------------------------------------------------------------------------------
// EXAMPLES FROM OTHERS:
play{a=LFSaw;Splay ar:HPF.ar(MoogFF.ar(a.ar(50*b=(0.999..9))-Blip.ar(a.ar(b)+9,b*99,9),a.ar(b/8)+1*999,a.ar(b/9)+1*2),9)/3}//


play{f={|o,i|if(i>0,{SinOsc.ar([i,i+1e-4]**2*f.(o,i-1),f.(o,i-1)*1e-4,f.(o,i-1))},o)};f.(60,6)/60}//


a=LFTri;play{|f=99|Pan2.ar(a.ar(f+{200.rand+216}.dup(8),{-2pi.rand+2pi}.dup(8),0.01+a.ar({0.01.rand+0.1}.dup(8),0,0.1)),0)}//


play{a=SinOsc.ar(LFNoise0.ar(10).range(100,1e4),0,0.05)*Decay.kr(Dust.kr(1));GVerb.ar(a*LFNoise1.ar(40),299,400,0.2,0.5,50,0,0.2,0.9)}//#


// #supercollider #sc208

play{ar(Splay,ar(RLPF,ar(a=VarSaw,ar(a,(c=4/4/4)/4,c,c).max*4+44,4,ar(a,[4,4/44,c*4]).abs),44*44,c)*ar(Line,dur:4)+ar(PitchShift,ar(CombC,b=ar(a,f=ar(a,d=c*4/44.4,c,-4.sin,4).max.ceil*44+44,4,ar(a,[44-4,4,44+4]/4.44))/4,c,c*4/f+d),4-c,[4+4+4,4*4,4+4],c,c,c+c))/4}//

// --------------------------------------------------------------------------------------------------
// RECORDING EXAMPLE:
s.record("".resolveRelative ++ "002-lfsaw-polyrhythms-bounce.aiff", numChannels:2)
s.stopRecording;

{|i|j=i+2/100;j.postln;}!12

// polyrhthms created by 16 lf pulses at different rates
play{GVerb.ar(CombC.ar(Splay,ar({|i|j=i+1;(LFPulse.ar(LFSaw.kr(j/9999)+1.1,width:0.04)*Pulse.ar(j*99)/(9+i))}!16,0.2),0.5,0.5,8))}

// saw changes ring freq with overlapping polyrhythms
play{FreeVerb2.ar(*Splay.ar({|i|Ringz.ar(Decay.ar(Impulse.ar(r=i+1/4),1/r,Crackle.ar/6),LFSaw.ar(f=r/(i+2*3),1)+2*3**4,f)}!16,0.4)*EnvGen.kr(Env.linen(30,90,30)))}
// (variation of the same)
play{FreeVerb2.ar(*Splay.ar({|i|Formlet.ar(Impulse.ar(r=i+1/8),LFSaw.ar(f=r/(i+2*3),1)+2*3**4,0.01,1/r/2)}!16,0.4))}

// Pulse waves controlling freq:
(
f = 10.collect{ExpRand(300, 20000)};
)

(
play{
	Splay.ar(
		{
			|i|
			f={exprand(99,4000)}!a=14;
			f = f ++ (f*2) ++ (f*i);
			g = f*(LFPulse.ar(i/2, 0, 1/i, 1/a)+2);
			t = (SinOsc.kr(1/i)+1.4)!a;
			DynKlank.ar(`[g, nil, t],
				// LFPulse.ar(i/2, 0, 1/i, 1/a)
				LFSaw.ar(i/8)/a +
				Dust.ar(1)/9
				// Crackle.ar(2.0, 1/a/i)
			);
		}!8,0.4
	)
}
)

Impulse





// variant of the above:
g=2*f=88;l=LFPulse;play{y=kr(XLine,1,6,f);ar(CombC,ar(Saw,ar(l,1!2)*f+f+(ar(l,y/9)*f+g)+(ar(l,y)*f)+(ar(l,1/y)*g)+(ar(l,y*2)*g))*(6-y)/22)}

(1..4).choose;

// ringing pulses
(
n=(1..24);
f=99;
play{
	Splay.ar(
	n.scramble.collect({|i|
	Ringz.ar(LFPulse.ar(n.choose, 0, 0.1*n.choose)
				+Crackle.ar(2.0,0.2)
				*(Crackle.ar(2.0,0.1)+0.4)
				*LFSaw.ar(n.choose/(i+1*8))
				,
				f*i,0.4,0.2)
	}),0.8);
}
)

(
play{
	{|f|
		{|i|ar(SinOsc,440*(f+1)*((i/9)+1) )*0.1}!1
		*ar(LFSaw, 0.2 * (f+1))
	}!8
}
)



// resonators in stacked fifths fading in and out
play{CombN.ar(Splay.ar(DynKlank.ar(`[3/2**(8..0)*440,{|b|SinOsc.ar(b/2+1/444)/(9-b)/6}!9.abs,1],PinkNoise.ar/29+Dust2.ar(2!2)),0.4))}

// delays time a series of 3 sweeps and saw tones
play{GVerb.ar(sum({|i|e=Env.perc(4,0,1,4).ar;DelayN.ar(QuadN.ar(e+1*3**j=i+3)*e+(Saw.ar(5**j,Env([0,0,1,0],[4,0.1,1]).kr)),i*8,i*8)}!3)/8)}
s.reboot; // delays use a lot of resources... may need to reboot after.

// choose between saw waves at various harmonics, with sweep filter and echo
play{GVerb.ar(CombN.ar(RLPF.ar(TChoose.ar(Dust.ar(99),{|i|Saw.ar(i+4*22)}!9),LFNoise1.kr(1)+2*3**4,1)*Env([0,1,1,0],[9,22,9]).ar/9,4,4,48))}

// lf pulse creates rhythms at various harmonics
play{FreeVerb2.ar(*CombC.ar(Splay.ar(({|i|Slew.ar(LFPulse.ar(i/22)*Saw.ar(f=66*i)*Env([0,1,1,0],[11,22,0]).ar/2,f,f)}!16).scramble),1,1,4))}

// lf pulse creates back and forth between notes in harmonic series, with harmonic, lag, rate, and on/off based on loop, timed envelope to end
l=LFPulse;play{GVerb.ar(sum({|i|Saw.ar(Lag.kr(l.kr(i/16,i-1/2),3/i)+1*99*i)*l.kr(1/8/i,0.5)*0.04}!16)*Env([0,0,1,1,0],[8,8,40,0.1]).ar)}

// harmonies/lines based on randomly choosing just-intuned intervals in relation to original freq
play{Splay.ar(({|i|l=0.3*(i+1);FreeVerb.ar(RLPF.ar(Pulse.ar(f=220*Lag.kr(TChoose.kr(Dust.kr(0.5*t=2**i,2,-1),DC.kr(t)/((1..4)+i))))*EnvGen.ar(Env.perc(0.01,l-0.01).circle)*LFNoise2.kr(0.1*((i+1)**1.5),n=0.4,n),f*LFNoise1.kr(n,4,4.1),n))}!7).scramble,0.6)*AmpCompA.kr(f,220)};


1e-3;


// FAKE DOT -> !!!
x=60;y=65;a=[x,x,62,x];play{GrainSin.ar(1,Impulse.ar(8),1/9,Dseq(a++[y,64,0]++a++[67,y,0,x,x,72,69,y,y,64,62,70,70,69,y,67,y]).midicps)%4e0}

(
g=2*f=88;l=LFPulse;
play{y=kr(XLine,1,6,f);


	Formant.ar(88, 88*2, LFPulse.kr(y/2,0,0.5,80,400));

}
)

1!2*2;




play{SinOsc(440)}


play{Resonz.ar(Crackle.ar(2!2),88,1/999,22);}

3.do{|i| i.postln;};

(
SynthDef(\a, { |freq|
	Out.ar(0,
		SinOsc.ar(freq)
		* EnvGen.kr(Env.perc,doneAction:2);
	);
}).add;
p=Pseq;
l=[2,2,4,5];
Pbind(\instrument,\a,\note, Pseq(l,8), \dur, 0.5).play;

)

	i = [1,2,3,4];

	[1,2,3]


play{ PinkNoise.ar(0.6!2) }

play{PinkNoise.ar!2}

	f=1;
	f=f*2;
	(
		f=220;l=LFPulse;play{
			Splay.ar(
				{|i|
					Saw.ar(
						Lag.kr(l.kr(i/8,i-1/2),3/i)*f*i+f
					)*l.kr(1/8/i,0.5)
				}!16, 0.4, 0.1
			)
		}
	)

	(
f=220;
l=LFPulse;
play{
    Splay.ar(
        {|i|
            Pulse.ar(
                Lag.kr(l.kr(1)*f*i+f,0.2) // this is the freq
            )
        }!4, 0.4, 0.2
    )
}
)


(
// modal space
// mouse x controls discrete pitch in dorian mode
var scale, buffer;
scale = FloatArray[0, 2, 3.2, 5, 7, 9, 10]; // dorian scale
buffer = Buffer.alloc(s,7,1,{|b|b.set[0,0,1,2,2,4,3,5,4,7,5,9])});

play({
    var mix;

    mix =

    // lead tone
    SinOsc.ar(
        (
            DegreeToKey.kr(
                buffer.bufnum,
                MouseX.kr(0,15),        // mouse indexes into scale
                12,                    // 12 notes per octave
                1,                    // mul = 1
                72                    // offset by 72 notes
            )
            + LFNoise1.kr([3,3], 0.04)    // add some low freq stereo detuning
        ).midicps,                        // convert midi notes to hertz
        0,
        0.1)

    // drone 5ths
    + RLPF.ar(LFPulse.ar([48,55].midicps, 0.15),
        SinOsc.kr(0.1, 0, 10, 72).midicps, 0.1, 0.1);

    // add some 70's euro-space-rock echo
    CombN.ar(mix, 0.31, 0.31, 2, 1, mix)
})
)




-1+2*3**4;
1+2*3**4;

{|i|Saw.ar(i+1*99)}!4

play{Saw.ar*Env([1,1,0],[8,6]).ar/4};

play{GVerb.ar(QuadC.ar(880)/9)}

9.rand+1/(9.rand+1);

(play{
Saw.ar*MouseY.kr(0,4)fold2:0.4
})

{ QuadC.ar(MouseX.kr(200, 8800)) * 0.3 }.play(s);

Env([0,0.4,0,0.6],0.5,\step).ar.

b=1;
b=b+1;
0.4.rand;

p=Env.perc;
p(1, 0.01, 1, 4)

a=(1..8).scramble/8;
Array.rand(7, 0.0, 1.0);

1+1*3**5
0+1*3**5
a=(1,4..20);
a +. [10,20];

[1,2,3,4] +* [0,1];

[3]++0.1!43


(play{
	Pulse.ar(440,0,Lag.kr(Env({0.4.rand}!22*.x[1,0],[3]++(0.1!43),\step).kr,0.01));
})

Env.perc(1, 0.01, 1, 4).ar;
Env.pairs({ { 1.0.rand } ! 2 } ! 16).plot;

Saw
Pulse

CombC



sum({|i|i}!7);

(
play{
sum({|i|e=Env.perc(4,0,1,4).ar;
DelayN.ar(
QuadN.ar(e+1*3**j=i+3)*e+
(Saw.ar(5**j,Env([0,0,1,0],[3.9,0.01,3]).kr)/2),
i*8,i*8
)
}!3)!2/2
}
)

i=4;
0+2*5**j=i+3/2;
1+2*5**j=i+3/2;
14**j;

5**4;
5**5;

{|i|i}!3
{0.2.rand}!22*.x[0,1].[8]++(0.1!43)

0**1;

s.freeAll;

LFCub.ar.plot;

(1)*(1);

LFNoise2
Dshuf
Ball
play{Dust.ar}
Klang
h;
h=2+h;
h;

{LFSaw.ar(iphase:1)}.plot;

-1+2*4**3;
1+2*4**3;

[1,2,3].sum;



sum(5.collect{|i|[0,0]+i+2**2;});


5.do{|i|f.value(i+2**2,4.rand+1)}



{|n=9,r=1|Ringz.ar(Decay.ar(Impulse.ar(r),r,PinkNoise.ar),LFSaw.ar(r/n,1)+2*3**4,0.02)}.play;


// *(f.value!2+f.value(22,4)+f.value(9,3))/4
//f={|r=1,t=1|RLPF.ar(Decay.ar(Impulse.ar(r),r,WhiteNoise.ar),LFTri.ar(t)+2*3**4,9)};
// noise, filters, and envelopes
(

f={|n,r|Ringz.ar(Decay.ar(Impulse.ar(r),1/r,Crackle.ar),LFSaw.ar(r/n,1)+6**4,r/n)};
play{
FreeVerb.ar(
5.collect{|i|f.value(i+2**2,i+1)}.sum!2/4
)
}

)





99**2;

l=LFNoise0;
[a=4,a*2,a*3,a*4];

[400] ++ [1,2,3];

f=90*x=4+1;
f;
x;

Impulse
Impulse

(
play{
f=99;x=Line.kr(9,0,60);
Splay.ar(
CombC.ar(
DynKlank.ar(`[[f]++(LFNoise0.ar.range(f,f+(x*f))*(5..1)),1/(6..1),1!6],
Dust2.ar(x!8)/(2+x)),
1,1,9)
)}
)

f={29.collect{|b|b+1*2/999}.scramble};
f.value;

SinOsc.ar(29.collect{|b|b+1/44}.scramble).abs

(0..9)**1.5+1*220;

4.collect{|b|b;}


[-1,0,1].abs;






(
{
    var f, sf, g;
    sf = LFNoise0.ar(MouseX.kr(1, 100, 1));
    g = MouseY.kr(0.1, 10, 1);
    f = Ball.ar(sf, g, 0.01, 0.01);
    f = f * 140 + 500;
    SinOsc.ar(f, 0, 0.2)
}.play;
)

(
{     var m0, m1, m2, m3, d, k, inforce;
    d = MouseY.kr(0.00001, 0.01, 1);
    k = MouseX.kr(0.1, 20, 1);
    inforce = K2A.ar(MouseButton.kr(0,1,0)) > 0;
    m0 = Spring.ar(inforce, k, 0.01);
    m1 = Spring.ar(m0, 0.5 * k, d);
    m2 = Spring.ar(m0, 0.6 * k + 0.2, d);
    m3 = Spring.ar(m1 - m2, 0.4, d);
    SinOsc.ar(m3 * 200 + 500, 0, 0.2) // modulate frequency with the force

}.play;
)