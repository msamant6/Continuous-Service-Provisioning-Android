import csv
with open("/home/varun/spatialalarms/dataserver/final_yelp.csv") as f:
    reader = csv.reader(f)
    next(reader,None)
    for row in reader:
        print(row)
        _, created = Location.objects.get_or_create(
        name = row[0],
        url = row[1],
        phone = row[2],
        latitude = float(row[3]),
        longitude = float(row[4]),
        city = row[5],
        pin_code = row[6])
