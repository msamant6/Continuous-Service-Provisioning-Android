# -*- coding: utf-8 -*-
# Generated by Django 1.10.3 on 2016-11-15 22:36
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('dataserver', '0003_auto_20161115_2234'),
    ]

    operations = [
        migrations.AlterField(
            model_name='location',
            name='pin_code',
            field=models.CharField(blank=True, max_length=10),
        ),
    ]
