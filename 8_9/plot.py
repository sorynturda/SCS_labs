import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

file_path = "C:\\Users\\Sorin Turda\\Desktop\\labs\\SCS_labs\\8_9\\04-12-22_temperature_measurements.csv"

def process_temp(df):
	for col in df.columns:
		if col != 'Timestamp':
			df[col] = (df[col] * 100).round().astype(int)
	return df

def csum_alg(x, threshold=200, drift=50):
	n = len(x)
	S = [0] * n
	g_plus = np.zeros(n)
	g_minus = np.zeros(n)
	t_abnormal = []
	for t in range(1, n):
		S[t] = x[t] - x[t-1]
		g_plus[t] = max(g_plus[t-1] + S[t] - drift, 0)
		g_minus[t] = min(g_minus[t-1] - S[t] - drift, 0)
		if g_plus[t] > threshold or g_minus[t] > threshold:
			t_abnormal.append(t)
			g_plus[t] = 0
			g_minus[t] = 0
	return t_abnormal

def ex2(df):
	for source in df.columns:
		if source == 'Timestamp':
			continue

		anomalies = csum_alg(df[source].values)

		plt.figure(figsize=(12, 6))

		plt.plot(df[source], label='Temperature', color='blue')
		if anomalies:
			plt.scatter(anomalies, df[source][anomalies], color='red', label='anomalies', zorder=5)
		plt.title(f"{source}")
		plt.xlabel("Time")
		plt.ylabel("Temperature")
		plt.grid(True)
		plt.show()

def ex3(df):
	for source in df.columns:
		if source == "Timestamp":
			continue
		file_name = f"{source}_data.bin"
		data.astype(np.int32).toFile(file_name)


def main():
	df = pd.read_csv(file_path, parse_dates=['Timestamp'])
	df = process_temp(df)
	ex2(df)
	ex3(df)

if __name__ == "__main__":
	main()
