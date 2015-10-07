function plotMetrics(data)
    function [] = plotHist(data1, data2, binwidth)
        display 'here'
        dataMin = min([data1 data2]); dataMax = max([data1 data2]);
        h1 = histogram(data1, 'BinLimits', [dataMin dataMax], 'BinWidth', binwidth, 'Normalization', 'probability');%, bins);
        hold on;
        h2 = histogram(data2, 'BinLimits', [dataMin dataMax], 'BinWidth', binwidth, 'Normalization', 'probability');%, bins);
    end
    function [] = plotHistInt(data1, data2)
        [cnts1, edges1] = histcounts(data1, 'BinMethod', 'integers');
        [cnts2, edges2] = histcounts(data2, 'BinMethod', 'integers');
        cnts1 = cnts1.*(edges1(1:end-1)+.5);
        cnts2 = cnts2.*(edges2(1:end-1)+.5);
        bar(cnts1,1,'FaceColor', [0    0.4470    0.7410], 'FaceAlpha', 0.6);
        hold on;
        bar(cnts2,1,'FaceColor', [0.8500    0.3250    0.0980], 'FaceAlpha', 0.6);
    end
    function f = flatten(arr, func)
        tmp = arrayfun(func, arr, 'UniformOutput', false);
        f = [tmp{:}];
    end
    if isstr(data)
        s = readTSV(data);
    else
        s = data;
    end
    % numFeatures seed  similarity  emptiness   rows  cols  strat threshes numEpochs   beforeSimilarity  beforeMooreClustering   beforeNeumannClustering afterSimilarity   afterMooreClustering afterNeumannClustering
    
    figure(1); clf;
    bs = flatten(s, @(x) x.beforeSimilarity');
    as = flatten(s, @(x) x.afterSimilarity');
    plotHist(bs, as, 0.1);
    xlabel('Similarity score');
    ylabel('Counts');
    title(sprintf('Similarity scores\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',s(1).numFeatures,s(1).emptiness*100,s(1).cols,s(1).rows,s(1).strat(:)));
    legend('Initial similarity scores','Final similarity scores','Location','northwest');
    %meanBeforeSimilarity=mean(s.beforeSimilarity);
    %meanAfterSimilarity=mean(s.afterSimilarity);
    %maxsim=max([hist(s.beforeSimilarity) hist(s.afterSimilarity)]);
    %text(5,(maxsim+2),strcat('Initial mean = ',meanBeforeSimilarity,', final mean = ',meanAfterSimilarity));
    saveas(gcf,'Similarity.png');
    %clf;
    
    figure(2); clf;
    bm = flatten(s, @(x) x.beforeMooreClustering');
    am = flatten(s, @(x) x.afterMooreClustering');
    plotHistInt(bm,am);
    xlabel('Moore Cluster Size');
    ylabel('Counts');
    title(sprintf('Moore cluster sizes\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',s(1).numFeatures,s(1).emptiness*100,s(1).cols,s(1).rows,s(1).strat(:)));
    legend('Initial Moore cluster sizes','Final Moore cluster sizes');
    %meanbmoore=mean(s.beforeMooreClustering);
    %meanamoore=mean(s.afterMooreClustering);
    %maxmoore=max([hist(s.beforeMooreClustering) hist(s.afterMooreClustering)]);
    %text(5,(maxmoore+2),strcat('Mean after 0 epochs = ',meanbmoore,', mean after ',epochs,' epochs = ',meanamoore));
    saveas(gcf,'Moore.png');
    %clf;
       
    figure(3); clf;
    bn = flatten(s, @(x) x.beforeNeumannClustering');
    an = flatten(s, @(x) x.afterNeumannClustering');
    plotHistInt(bn,an);
    xlabel('von Neumann Cluster Size');
    ylabel('Counts');
    title(sprintf('von Neumann cluster sizes\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',s(1).numFeatures,s(1).emptiness*100,s(1).cols,s(1).rows,s(1).strat(:)));
    legend('Initial von Neumann cluster sizes','Final von Neumann cluster sizes');
    %meanbneumann=mean(s.beforeNeumannClustering);
    %meananeumann=mean(s.afterNeumannClustering);
    %maxneumann=max([hist(s.beforeNeumannClustering) hist(s.afterNeumannClustering)]);
    %text(5,(maxneumann+2),strcat('Mean after 0 epochs = ',meanbneumann,', mean after ',epochs,' epochs = ',meananeumann));
    saveas(gcf,'Neumann.png');
    %clf;
end
