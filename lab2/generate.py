from kashgari.embeddings import BERTEmbedding
from kashgari.tasks.labeling import BiLSTM_CRF_Model
from kashgari.utils import load_model
import json
import csv

model_save_path = 'trained_model/saved_model'
test_data_path = "data/test_data.json"
output_path = 'submission.csv'
tag_map = {"LAB": "实验室检验",
            "IMAGE": "影像检查",
            "OP": "手术",
            "DISEASE": "疾病和诊断",
            "MEDICINE": "药物",
            "ANTAMY": "解剖部位"}

def parse_data(file_path):
    data_x, data_y = [], []

    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.read().splitlines()

        x, y = [], []
        for line in lines:
            rows = line.split(' ')
            if len(rows) == 1:
                data_x.append(x)
                data_y.append(y)
                x = []
                y = []
            else:
                x.append(rows[0])
                y.append(rows[1])
    return data_x, data_y

model = load_model('trained_model/saved_model[epochs=10, bath_size=50]')
print("Load model finshed")

fo = open(output_path, "w")
count = 0
with open(test_data_path, 'r', encoding='utf-8') as f:
    lines = f.read().splitlines()
    fo.write("textId,label_type,start_pos,end_pos\n")
    for line in lines:
        data = json.loads(line)
        textId = data['textId']
        # if textId <= 598:
            # continue
        print("> " + str(textId))
        text = data['originalText']
        slist = text.split('。')
        pred_results = model.predict_entities(slist)
        pos = 0
        for pred in pred_results:
            labels = pred['labels']
            for l in labels:
                t = l['entity']
                if t == '<PAD>':
                    continue
                label_type = tag_map[t]
                spos = pos + l['start']
                epos = pos + l['end'] + 1
                if l['value'][:-1] == '，':
                    epos = epos - 1
                fo.write(str(textId) + ',' + label_type + ',' + str(spos) + ',' + str(epos) + '\n')
            pos = pos + len(pred['text_raw']) + 1
    count = count + 1
fo.close()
print('line count: ' + str(count))

# 预测
# while True:
#     s = input("> ")
#     if s == "OK":
#         break
#     # pred_results = model.predict_entities(list(s))
#     sl = s.split("。")
#     # sl = [s]
#     print(sl)
#     pred_results = model.predict_entities(sl)
#     for p in pred_results:
#         for l in p['labels']:
#             print(l['entity'] + ": " + l['value'])
#     print(pred_results)

