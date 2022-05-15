import csv
import json


with open('data.json','r') as f:
    content = json.load(f)

info = []


date_count = {}

hour_count = {}

for entry in content:
    if 'probes' in content[entry]:
        for data in content[entry]['probes']:
            information = {}

            time_parts = data['date'].strip('\n').split(' ')
            daTa = ''
            day = time_parts[0]
            if len(time_parts) == 6:
                if time_parts[1] == 'Apr':
                    daTa = time_parts[5] + '-4-' + time_parts[3]
                else:
                    daTa = time_parts[5] + '-5-' + time_parts[3]
                hour = time_parts[4][:2]
            else:
                if time_parts[1] == 'Apr':
                    daTa = time_parts[4] + '-4-' + time_parts[2]
                else:
                    daTa = time_parts[4] + '-5-' + time_parts[2]
                hour = time_parts[3][:2]
            
            if day not in date_count:
                date_count[day] = 1
            else:
                date_count[day] += 1

            if hour not in hour_count:
                hour_count[hour] = 1
            else:
                hour_count[hour] += 1

            information['mac_address'] = data['mac']
            information['date'] = daTa
            information['day'] = day
            information['hour'] = hour

            info.append(information)
            
# print(date_count)
# print(hour_count)
# print(info)

fields = ['mac_address','date','day','hour']

with open('info.csv','w') as f:
    writer = csv.DictWriter(f,fieldnames=fields)
    writer.writeheader()
    writer.writerows(info)