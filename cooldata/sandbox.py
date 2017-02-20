import pandas as pd
import numpy as np
import abjad

print("yo!")


# print(s.iat[6])

class Rhythms(object):
    rhythm_cells = pd.DataFrame({
        "fast":pd.Series([2,4,6,8]),
        "slow":pd.Series([1,-2,3,4,1,-2,3,4]),
        })

    rhythm_phrases = pd.DataFrame({
        "abba":pd.Series(["fast","slow","slow","fast"]),
        "baab":pd.Series(["slow","fast","fast","slow"]),
        })


r = Rhythms()



print(r.rhythm_cells["fast","slow"] )