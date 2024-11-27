import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

file_path = "c:\\Users\\Sorin Turda\\Desktop\\SCS_labs\\8_9\\04-12-22_temperature_measurements.csv"

def process_temp(df):
	df[" LM35DZ"] = df[" LM35DZ"].apply(lambda x: round(x*100))
	df[" BMP180"] = df[" BMP180"].apply(lambda x: round(x*100))
	df[" Thermistor"] = df[" Thermistor"].apply(lambda x: round(x*100))
	df[" DHT22"] = df[" DHT22"].apply(lambda x: round(x*100))
	df[" DS18B20"] = df[" DS18B20"].apply(lambda x: round(x*100))
	return df

def csum_alg(x, threshold=200, drift=50):
	n = len(x)
	S = [0] * n
	g_plus = [0] * n
	g_minus = [0] * n
	t_abnormal = []
	timestamps = []
	for t in range(1, n):
		S[t] = x[t] - x[t-1]
		g_plus[t] = max(g_plus[t-1] + S[t] - drift, 0)
		g_minus[t] = min(g_minus[t-1] - S[t] - drift, 0)
		if g_plus[t] > threshold or g_minus[t] > threshold:
			t_abnormal.append(t)
			timestamps.append(x[t])
			g_plus[t] = 0
			g_minus[t] = 0
	return t_abnormal, timestamps

def ex2(df):
	for source in df.columns:
		if source == 'Timestamp':
			continue
		# data = csum_alg(df[source].tolist())
		y_points, x_points= np.array(df[source].tolist()), np.array([x for x in range(1701)])
		plt.plot(x_points, y_points)
		plt.title(f"{source}")
		plt.xlabel("index")
		plt.ylabel("values")
		plt.show()


df = pd.read_csv(file_path)
df = process_temp(df)

ex2(df)
