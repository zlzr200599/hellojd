import time
def time_str_to_timestamp(time_str):
    time_tuple = time.strptime(time_str, '%Y/%m/%d %H:%M')
    time_stamp = time.mktime(time_tuple)
    return round(time_stamp)
	
import csv
def make_collect(file_name = "collect.csv",output_name = "collect2bitmap.json"):
    print("collect")
    data = []
    with open(file_name, newline='',encoding="utf8") as csvfile:
        reader = csv.DictReader(csvfile)
        for i, row in enumerate(reader):
            src = row['始发乡/街道编码'] or row['始发县/区编码'] or row['始发城市编码'] or row['始发省份编码'] 
            dst = row['目的城市编码'] or row['目的省份编码']
            ddl, delay = row['揽收截止时间'], row['揽收时长']
            enable, disable = time_str_to_timestamp(row['生效日期']),time_str_to_timestamp(row['失效日期'])
            enable, disable = str(enable), str(disable)
            out = {'src':src, 'dst':dst, 'ddl':ddl, 'delay':delay, 'enable':enable, 'disable':disable}
#             if out['src'] == "138387" and out['dst']=="":
#                 print(out)
                
            data.append(out)
#     #             print(out)
#             if i == 100000:
#                 print("all process sample: ",i)
#                 break
            print(i, end = "\r")

    addr_dict = {}
    start_tuple = time.strptime("2020-04-29", '%Y-%m-%d')
    end_tuple = time.strptime("2021-12-06", '%Y-%m-%d')
    start_day, end_day = round((time.mktime(start_tuple) + 3600*8)//86400),round((time.mktime(end_tuple) + 3600*8)//86400) 

    for i, row in enumerate(data):
        a, b = (int(row['enable']) +3600*8)//86400,(int(row['disable'])+ 3600*8)//86400
        if not ((b <= start_day) or (a >= end_day)):
            assert a <= b, "wrong"
            addr_dict[row['src']+ ":" + row['dst']] = [0]* (end_day-start_day)
    # print("valid addr:", len(addr_dict))
    # print(addr_dict)
    ddl_dict = {'16:00':1, '17:00':2, '17:30':3, '18:00':4, '19:00':5, '20:00':6}
    delay_dict = {'5': 5, '4': 4, '3':3,'2':2, '1':1}
    for i, row in enumerate(data):
        if row['src']+ ":" + row['dst'] in addr_dict:
            a = max(0, (int(row['enable']) +3600*8)//86400 - start_day)
            b = min(end_day-start_day, (int(row['disable'])+ 3600*8)//86400 -start_day)
            if a < b:
                addr_dict[row['src']+ ":" + row['dst']][a:b] = [delay_dict[row['delay']]*8 + ddl_dict[row['ddl']]]* (b - a)
#                 if row['src'] == "138387" and row['dst']=="":
#     #                 print( addr_dict[row['src']+ ":" + row['dst']][a:b],)
#                     print(delay_dict[row['delay']], ddl_dict[row['ddl']], a, b)
    print("valid addr:", len(addr_dict))
    # print(addr_dict)
    # for i, (k, v) in enumerate(addr_dict.items()):
    #     assert sum(v) > 0, "wrong"
    #     print(sum([1 for i in v if i]),len(v))
    import json
    with open(output_name, 'w') as fp:
        json.dump(addr_dict, fp)
make_collect()


def make_route(file_name = "route.csv",output_name = "route2bitmap.json"):
    print("route")
    data = []
    with open(file_name, newline='',encoding="utf8") as csvfile:
        reader = csv.DictReader(csvfile)
        for i, row in enumerate(reader):
            src = row['始发城市编码'] or row['始发省编码'] 
            dst = row['目的乡/镇/街道编码'] or row['目的区/县编码'] \
            or row['目的城市编码'] or row['目的省编码']
            delay = row['配置承诺时效']
            enable, disable = time_str_to_timestamp(row['生效时间']),time_str_to_timestamp(row['失效时间'])
            enable, disable = str(enable), str(disable)
            out = {'src':src, 'dst':dst, 'delay':delay, 'enable':enable, 'disable':disable}
            data.append(out)
#             print(out)
#             if i == 5000:
#                 print(i)
#                 break
            print(i, end = "\r")

    addr_dict = {}
    start_tuple = time.strptime("2020-04-29", '%Y-%m-%d')
    end_tuple = time.strptime("2021-12-06", '%Y-%m-%d')
    start_day, end_day = round((time.mktime(start_tuple) + 3600*8)//86400),round((time.mktime(end_tuple) + 3600*8)//86400) 

    for i, row in enumerate(data):
        a, b = (int(row['enable']) +3600*8)//86400,(int(row['disable'])+ 3600*8)//86400
        if not ((b <= start_day) or (a >= end_day)):
            assert a <= b, "wrong"
            addr_dict[row['src']+ ":" + row['dst']] = [0]* (end_day-start_day)
    # print("valid addr:", len(addr_dict))
    # print(addr_dict)
    route_time_list = ['4D2200', '11D2359', '7D1800', '2D1700', '5D1500', '4D1500', 
                       '8D2200', '5D1800', '2D1500', '3D2200', '8D1500', '8D2359', 
                       '5D2200', '6D1700', '12D2359', '5D2359', '21D2200', '10D1500', 
                       '13D2359', '2D1800', '6D2359', '12D2200', '9D2359', '4D1700', 
                       '11D1500', '3D2359', '4D2359', '15D1500', '20D2200', '9D1500', 
                       '16D1500', '17D2200', '17D1500', '5D1700', '3D1700', '18D2200', 
                       '9D2200', '16D2200', '13D1500', '11D2200', '15D2200', '3D1800', 
                       '3D1500', '7D2200', '6D1800', '4D1800', '10D2359', '14D1500', 
                       '22D2200', '14D2200', '7D2359', '7D1700', '7D1500', '2D2200', 
                       '14D2359', '6D1500', '6D2200', '19D2200', '13D2200', '10D2200']
    delay_dict = {v:k for k, v in enumerate(route_time_list, 1)}
    for i, row in enumerate(data):
        if row['src']+ ":" + row['dst'] in addr_dict:
            a = max(0, (int(row['enable']) +3600*8)//86400 - start_day)
            b = min(end_day-start_day, (int(row['disable'])+ 3600*8)//86400 -start_day)
            if a < b:
                addr_dict[row['src']+ ":" + row['dst']][a:b] = [delay_dict[row['delay']]]* (b - a)
    #             print(delay_dict[row['delay']], ddl_dict[row['ddl']], a, b)
    print("valid addr:", len(addr_dict))
    # print(addr_dict)
    # for i, (k, v) in enumerate(addr_dict.items()):
    #     assert sum(v) > 0, "wrong"
    #     print(sum([1 for i in v if i]),len(v))
    import json
    with open(output_name, 'w') as fp:
        json.dump(addr_dict, fp)
make_route()


def make_dispatch(file_name = "dispatch.csv", output_name = "dispatch2bitmap.json"):
    print("dispatch")
    data = []
    with open(file_name, newline='',encoding="utf8") as csvfile:
        reader = csv.DictReader(csvfile)
        for i, row in enumerate(reader):
            dst = row['乡/街道编码'] or row['县/区编码'] or row['城市编码'] or row['省份编码']
            delay = row['派送时长加时']
            enable, disable = time_str_to_timestamp(row['生效日期']),time_str_to_timestamp(row['失效日期'])
            enable, disable = str(enable), str(disable)
            out = { 'dst':dst, 'delay':delay, 'enable':enable, 'disable':disable}
            data.append(out)
#             print(out)
#             if i == 50:
#                 print(i)
#                 break
            print(i, end = "\r")

    addr_dict = {}
    start_tuple = time.strptime("2020-04-29", '%Y-%m-%d')
    end_tuple = time.strptime("2021-12-06", '%Y-%m-%d')
    start_day, end_day = round((time.mktime(start_tuple) + 3600*8)//86400),round((time.mktime(end_tuple) + 3600*8)//86400) 

    for i, row in enumerate(data):
        a, b = (int(row['enable']) +3600*8)//86400,(int(row['disable'])+ 3600*8)//86400
        if not ((b <= start_day) or (a >= end_day)):
            assert a <= b, "wrong"
            addr_dict[row['dst']] = [0]* (end_day-start_day)
    # print("valid addr:", len(addr_dict))
    # print(addr_dict)
    delay_dict = {'24':3, '0':1, '1':2}
    for i, row in enumerate(data):
        if row['dst'] in addr_dict:
            a = max(0, (int(row['enable']) +3600*8)//86400 - start_day)
            b = min(end_day-start_day, (int(row['disable'])+ 3600*8)//86400 -start_day)
            if a < b:
                addr_dict[row['dst']][a:b] = [delay_dict[row['delay']]]* (b - a)
    #             print(delay_dict[row['delay']], ddl_dict[row['ddl']], a, b)
    print("valid addr:", len(addr_dict))
    # print(addr_dict)
    # for i, (k, v) in enumerate(addr_dict.items()):
    #     assert sum(v) > 0, "wrong"
    #     print(sum([1 for i in v if i]),len(v))
    import json
    with open(output_name, 'w') as fp:
        json.dump(addr_dict, fp)
make_dispatch()