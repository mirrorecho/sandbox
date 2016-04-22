from datetime import datetime

filename="clusters classical groups (1)"

cases = (
        ( # (#1: #2; #3: #4) by \qcfs{#1}{#2}{#3}{#4}
        2, [], "\\qcfs{%s}{%s}{%s}{%s}", (":", [0,1]) # 
        ),
        ( # (#1; #2: #3; #4) by \qcfb{#1}{#2}{#3}{#4}
        3, [], "\\qcfb{%s}{%s}{%s}{%s}", (":", [1])
        ),
        ( # (#1; 0; #2; 0) by \dud{#1}{#2}
        4, [1,3], "\\dud{%s}{%s}" 
        ),
        ( # (#1; #2; 0; 0) by \dul{#1}{#2}
        4, [2,3], "\\dul{%s}{%s}" 
        ),
        ( # (#1; 0; 0; #2) by \dur{#1}{#2}
        4, [1,2], "\\dur{%s}{%s}" 
        ),
        ( # (0; #1; #2; 0) by \dld{#1}{#2}
        4, [0,3], "\\dld{%s}{%s}" 
        ),
        ( # (0; #1; 0; #2) by \dlr{#1}{#2}
        4, [0,2], "\\dlr{%s}{%s}" 
        ),
        ( # (0; 0; #1; #2) by \ddr{#1}{#2}
        4, [0,1], "\\ddr{%s}{%s}" 
        ),
        ( # (#1; #2; #3; 0) by \tcfl{#1}{#2}{#3}
        4, [3], "\\tcfl{%s}{%s}{%s}" 
        ),
        ( # (#1; 0; #2; #3) by \tcfr{#1}{#2}{#3}
        4, [1], "\\tcfr{%s}{%s}{%s}" 
        ),
        ( # (#1; #2; 0; #3) by \tcfu{#1}{#2}{#3}
        4, [2], "\\tcfu{%s}{%s}{%s}" 
        ),
        ( # (0; #1; #2; #3) by \tcfd{#1}{#2}{#3}
        4, [0], "\\tcfd{%s}{%s}{%s}" 
        ),
        ( # (#1; #2; #3; #4) by \qcf{#1}{#2}{#3}{#4}
        4, [], "\\qcf{%s}{%s}{%s}{%s}" 
        ),
        ( # (#1; #2; 0) by \dud{#1}{#2}
        3, [2], "\\dud{%s}{%s}" 
        ),
        ( # (#1; 0; #3) by \tcfr{#1}{}{#3}
        3, [1], "\\tcfr{%s}{}{%s}" 
        ),
        ( # (0; #2; #3) by \tcfr{}{#2}{#3}
        3, [0], "\\tcfr{}{%s}{%s}" 
        ),
        ( # (#1; #2; #3) by \tcfr{#1}{#2}{#3}
        3, [], "\\tcfr{%s}{%s}{%s}" 
        ),
    )

print("=================================================================")

with open(filename + ".tex", "rt") as in_file:
    text = in_file.read()

# print(text)
def zero_positions(items):
    positions = []
    for n,i in enumerate(items):
        if i.strip() == "0":
            positions.append(n)
    return positions

def process_innards(innards):
    items = innards.split(";")
    zeros = zero_positions(items)
    # print(innards)
    for case in cases:
        case_match=False # assume guilty until proven innocent
        
        if len(items)==case[0]: # makes sure matches count of ";" based list items

            if len(case)==4: # if case has 4th element, then need to check sub-lists
                # print(case)
                case_match = True # assume innocent until proven guilty
                for n in case[3][1]:
                    if not case[3][0] in items[n]:
                        case_match = False
                if case_match:
                    items_sub = []
                    for i, item in enumerate(items):
                        if i in case[3][1]:
                            items_sub.extend(item.split(case[3][0]))
                        else:
                            items_sub.append(item)
                    items = items_sub
            else:
                case_match = zero_positions(items)==case[1]
            
            if case_match:
                replaces = []
                for r in [i for i in range(len(items)) if i not in case[1]]:
                    replaces.append(items[r].strip())
                # print(items)
                # print(case)
                return case[2] % tuple(replaces)

open_finds = text.split("(")

for p in open_finds[1:]:
    if ")" in p:
        innards = p.split(")")[0]
        if innards.count(";") > 0:
            new_innards = process_innards(innards)
            if new_innards:
                text = text.replace("(" + innards + ")", new_innards)

with open(filename + "-" + str(datetime.now()) + ".txt", "w") as out_file:
    out_file.write(text)


