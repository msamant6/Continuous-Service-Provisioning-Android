from __future__ import unicode_literals

from django.db import models

class Location(models.Model):
    name = models.CharField(max_length = 200)
    url = models.URLField()
    phone = models.CharField(max_length = 15, blank=True)
    address = models.CharField(max_length = 200, blank=True)
    latitude = models.FloatField()
    longitude = models.FloatField()
    city = models.CharField(max_length = 100, blank=True)
    pin_code = models.CharField(max_length = 10, blank=True)
