from __future__ import unicode_literals

from django.apps import AppConfig

import pandas as pd
from pyspark import SparkContext
from pyspark.sql import SQLContext
from pyspark.sql import SparkSession

import numpy as np

sc = SparkContext('local','spatialspark')
sql_sc = SQLContext(sc)
df = pd.read_csv("/home/varun/spatialalarms/dataserver/yelp.csv")
df = df.replace(np.nan,"",regex = True)
sd = sql_sc.createDataFrame(df)
'''
sd = sql_sc.read.format('com.databricks.spark.csv')\
.options(header='true', inferschema='true')\
.load('file:///home/varun/spatialalarms/dataserver/yelp.csv')
'''
spark = SparkSession \
    .builder \
    .appName("My Spark SQL") \
    .config("spark.some.config.spatialalarms", "Spatialalarms Value") \
    .getOrCreate()

class DataserverConfig(AppConfig):
    name = 'dataserver'
