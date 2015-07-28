
// --------------------------------------------------------------------------------------------------
// EXAMPLES FROM OTHERS:
play{a=LFSaw;Splay ar:HPF.ar(MoogFF.ar(a.ar(50*b=(0.999..9))-Blip.ar(a.ar(b)+9,b*99,9),a.ar(b/8)+1*999,a.ar(b/9)+1*2),9)/3}//


play{f={|o,i|if(i>0,{SinOsc.ar([i,i+1e-4]**2*f.(o,i-1),f.(o,i-1)*1e-4,f.(o,i-1))},o)};f.(60,6)/60}//


a=LFTri;play{|f=99|Pan2.ar(a.ar(f+{200.rand+216}.dup(8),{-2pi.rand+2pi}.dup(8),0.01+a.ar({0.01.rand+0.1}.dup(8),0,0.1)),0)}//


play{a=SinOsc.ar(LFNoise0.ar(10).range(100,1e4),0,0.05)*Decay.kr(Dust.kr(1));GVerb.ar(a*LFNoise1.ar(40),299,400,0.2,0.5,50,0,0.2,0.9)}//#
// --------------------------------------------------------------------------------------------------


// saw changes ring freq with overlapping polyrhythms
play{FreeVerb2.ar(*Splay.ar({|i|Ringz.ar(Decay.ar(Impulse.ar(r=i+1/4),1/r,Crackle.ar/6),LFSaw.ar(f=r/(i+2*3),1)+2*3**4,f)}!16,0.4))}


// Pulse waves controlling freq:
g=2*f=88;l=LFPulse;play{y=XLine.kr(1,6,f);CombL.ar(Saw.ar(l.ar(1!2)*f+f+(l.ar(y/9)*f+g)+(l.ar(y)*f)+(l.ar(1/y)*g)+(l.ar(y*2)*g))*(6-y)/22)}

// resonators in stacked fifths fading in and out
play{CombN.ar(Splay.ar(DynKlank.ar(`[3/2**(8..0)*440,{|b|SinOsc.ar(b/2+1/444)/(9-b)/6}!9.abs,1],PinkNoise.ar/29+Dust2.ar(2!2)),0.4))}


12.collect{|i|(i+2*3)}
12.collect{|i|(i+2**2)}

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

(
play{
e=Env.perc(4,0.01,1,4).ar;
QuadN.ar(e+1*3**5)*e/2+
CombC.ar(
Pulse.ar(222,0.6,Env({0.2.rand}!44*.x[1,0],[3]++(0.1!87),\step).kr,0.01),
		1,1,44);

	// WhiteNoise.ar*e

// Env.circle({0.4.rand}!8,(1..8).scramble/8).ar
}
)

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