import json

fo = open("train_data_split_with_period.txt", "w")
with open("data/subtask1_training_part1.json", 'r', encoding='utf-8') as file:
    count = 0
    scount = 0
    tag_map = {"实验室检验": "LAB",
            "影像检查": "IMAGE",
            "手术": "OP",
            "疾病和诊断": "DISEASE",
            "药物": "MEDICINE",
            "解剖部位": "ANTAMY"}
            
    for line in file:
        data = json.loads(line)
        text = data["originalText"]
        i = 0
        j = 0
        l = len(data["entities"])
        if l == 0:
            continue
        label = data["entities"][j]
        tag = tag_map[label["label_type"]]
        for c in text:
            if i < label["start_pos"]:
                fo.write(c + " O\n")
            elif i == label["start_pos"]:
                fo.write(c + " B-" + tag + "\n")
            elif i < label["end_pos"]:
                fo.write(c + " I-" + tag + "\n")
            elif i == label["end_pos"]:
                j = j + 1
                if j < l:
                    label = data["entities"][j]
                    tag = tag_map[label["label_type"]]
                    if i == label["start_pos"]:
                        fo.write(c + " B-" + tag + "\n")
                    else:
                        fo.write(c + " O\n")
                else:
                    fo.write(c + " O\n")
            else:
                fo.write(c + " O\n")
            if c == '。':
                fo.write("\n")
                scount = scount + 1
            i = i + 1
        fo.write("\n")
        count = count + 1
        # if count == 10:
        #     break
fo.close()

print("item count: " + str(count))
print("senetence count:" + str(scount))