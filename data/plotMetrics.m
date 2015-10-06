function[]=plotMetrics(data)
    function nums = readNums(chars)
        nums = str2num(strjoin(cellstr(chars),','));
    end
    function [] = plotHist(data1, data2, binwidth)
        dataMin = min([data1 data2]); dataMax = max([data1 data2]);
        h1 = histogram(data1, 'BinLimits', [dataMin dataMax], 'BinWidth', binwidth, 'Normalization', 'probability');%, bins);
        hold on;
        h2 = histogram(data2, 'BinLimits', [dataMin dataMax], 'BinWidth', binwidth, 'Normalization', 'probability');%, bins);
    end
    function [] = plotHistInt(data1, data2)
        [cnts1, edges1] = histcounts(data1, 'BinMethod', 'integers');
        [cnts2, edges2] = histcounts(data2, 'BinMethod', 'integers');
        size(cnts1)
        size(cnts2)
        cnts1 = cnts1.*(edges1(1:end-1)+.5);
        cnts2 = cnts2.*(edges2(1:end-1)+.5);
        bar(cnts1,1,'FaceColor', [0    0.4470    0.7410], 'FaceAlpha', 0.6);
        hold on;
        bar(cnts2,1,'FaceColor', [0.8500    0.3250    0.0980], 'FaceAlpha', 0.6);
    end
    if isstr(data)
        s = tdfread(data);
    else
        s = data;
    end
    % numFeatures seed  similarity  emptiness   rows  cols  strat threshes numEpochs   beforeSimilarity  beforeMooreClustering   beforeNeumannClustering afterSimilarity   afterMooreClustering afterNeumannClustering
    numFeatures = s.numFeatures;
    seed = s.seed;
    similarity = s.similarity;
    emptiness = s.emptiness;
    rows = s.rows;
    cols = s.cols;
    strat = s.strat;
    threshes = s.threshes;
    numEpochs = s.numEpochs;
    beforeSimilarity=readNums(s.beforeSimilarity);
    beforeMoore=readNums(s.beforeMooreClustering);
    beforeNeumann=readNums(s.beforeNeumannClustering);
    afterSimilarity=readNums(s.afterSimilarity);
    afterMoore=readNums(s.afterMooreClustering);
    afterNeumann=readNums(s.afterNeumannClustering);
    
    figure(1); clf;
    plotHist(beforeSimilarity, afterSimilarity, 0.1);
    xlabel('Similarity score');
    ylabel('Counts');
    title(sprintf('Similarity scores\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',numFeatures(1),emptiness(1)*100,cols(1),rows(1),strat(1,:)));
    legend('Initial similarity scores','Final similarity scores','Location','northwest');
    meanBeforeSimilarity=mean(beforeSimilarity);
    meanAfterSimilarity=mean(afterSimilarity);
    maxsim=max([hist(beforeSimilarity) hist(afterSimilarity)]);
    %text(5,(maxsim+2),strcat('Initial mean = ',meanBeforeSimilarity,', final mean = ',meanAfterSimilarity));
    saveas(gcf,'Similarity.png');
    %clf;
    
    figure(4); clf;
    plotHist(beforeMoore,afterMoore, 1);
    figure(2); clf;
    plotHistInt(beforeMoore,afterMoore);
    xlabel('Moore Cluster Size');
    ylabel('Counts');
    title(sprintf('Moore cluster sizes\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',numFeatures(1),emptiness(1)*100,cols(1),rows(1),strat(1,:)));
    legend('Initial Moore cluster sizes','Final Moore cluster sizes');
    meanbmoore=mean(beforeMoore);
    meanamoore=mean(afterMoore);
    maxmoore=max([hist(beforeMoore) hist(afterMoore)]);
    %text(5,(maxmoore+2),strcat('Mean after 0 epochs = ',meanbmoore,', mean after ',epochs,' epochs = ',meanamoore));
    saveas(gcf,'Moore.png');
    %clf;
       
    figure(5); clf;
    plotHist(beforeNeumann,afterNeumann, 1);
    figure(3); clf;
    plotHistInt(beforeNeumann,afterNeumann);
    xlabel('von Neumann Cluster Size');
    ylabel('Counts');
    title(sprintf('von Neumann cluster sizes\n(features=%d, emptiness=%.0d%%, %dx%d, %s)',numFeatures(1),emptiness(1)*100,cols(1),rows(1),strat(1,:)));
    legend('Initial von Neumann cluster sizes','Final von Neumann cluster sizes');
    meanbneumann=mean(beforeNeumann);
    meananeumann=mean(afterNeumann);
    maxneumann=max([hist(beforeNeumann) hist(afterNeumann)]);
    %text(5,(maxneumann+2),strcat('Mean after 0 epochs = ',meanbneumann,', mean after ',epochs,' epochs = ',meananeumann));
    saveas(gcf,'Neumann.png');
    %clf;
end
