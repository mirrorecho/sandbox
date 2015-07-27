(
~libraryPath = "/home/randall/Echo/Sounds/Library/";

~breathFiles = [
    "breath-in/small-01.wav", 
    "breath-in/small-02.wav", 
    ];

~breaths=[];

~breathFiles.do { arg bFile;
    ~breaths = ~breaths ++ [Buffer.read(s, ~libraryPath ++ bFile)];
}
)


~breaths[1].play;