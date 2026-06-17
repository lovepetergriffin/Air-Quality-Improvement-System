<template>
  <div ref="chartBox" class="chart"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue';

const props = defineProps({
  gaugecolor: { type: String, default: '#32ccee' },
  gaugedata: { type: [Array, Number], default: 0 }
});

const chartBox = ref(null);
let chart = null;

const gaugeValue = computed(() => {
  if (Array.isArray(props.gaugedata)) {
    return Number(props.gaugedata[0]?.value || 0);
  }
  return Number(props.gaugedata || 0);
});

const renderChart = () => {
  if (!chart) return;
  chart.setOption({
    backgroundColor: 'transparent',
    series: [
      {
        type: 'gauge',
        min: 0,
        max: 100,
        radius: '88%',
        center: ['50%', '58%'],
        progress: { show: true, width: 10, itemStyle: { color: props.gaugecolor } },
        axisLine: { lineStyle: { width: 10, color: [[1, '#113f66']] } },
        axisTick: { show: false },
        splitLine: { distance: 2, length: 8, lineStyle: { color: '#4fb3d8', width: 1 } },
        axisLabel: { color: '#8fc7e5', distance: 14, fontSize: 10 },
        pointer: { width: 4, length: '58%', itemStyle: { color: '#e7fbff' } },
        detail: {
          valueAnimation: true,
          formatter: '{value}%',
          color: '#ffffff',
          fontSize: 18,
          offsetCenter: [0, '64%']
        },
        data: [{ value: Math.round(gaugeValue.value) }]
      }
    ]
  });
};

const resizeChart = () => chart && chart.resize();

onMounted(() => {
  chart = echarts.init(chartBox.value);
  renderChart();
  window.addEventListener('resize', resizeChart);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart);
  chart && chart.dispose();
});

watch(() => [gaugeValue.value, props.gaugecolor], renderChart);
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
