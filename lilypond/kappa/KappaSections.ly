
%{
#(define kappaSectionData (list (list "kappa-empty-part" '() ) ) )

kappaDefineSection = #(define-music-function (parser location sectionName music) 
	(string? ly:music?)
	(set! kappaSectionData (append kappaSectionData) )
)

kappaArrangeSections = #(define-music-function (parser location sectionList) 
	(lst?)
#{

#})


kappaDefineSectionMusic = #(define-music-function (parser location sectionName partName music) 
	(string? string? ly:music?)
#{

#})


kappaSectionsMusic = #(define-music-function (parser location partName) 
	(string?)
#{

#})

%}