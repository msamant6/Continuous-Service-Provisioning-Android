# -*- coding: utf-8 -*-
# Generated by Django 1.10.3 on 2016-11-15 22:03
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Location',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=200)),
                ('url', models.URLField()),
                ('phone', models.IntegerField()),
                ('address', models.CharField(max_length=200)),
                ('latitude', models.FloatField()),
                ('longitude', models.FloatField()),
                ('city', models.CharField(max_length=100)),
                ('pin_code', models.IntegerField()),
            ],
        ),
    ]
